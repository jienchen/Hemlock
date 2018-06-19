package app;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.servlet.http.Cookie;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import java.util.Base64;
import java.util.ArrayList;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import java.text.SimpleDateFormat;

import static app.JWT.EXPIRATION_TIME;
import static app.JWT.SECRET;
import static app.Constants.badWords;

@Controller
public class TvController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TvShowRepository tvRepository;

    @Autowired
    private WatchlistTvRepository watchlistTvRepository;

    @Autowired
    private BlacklistTvRepository blacklistTvRepository;

    @Autowired
    private TvReviewRepository tvReviewRepository;

    @Autowired
    private ReportedReviewTvRepository reportedReviewTvRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private TvCastRepository tvCastRepository;

    @Autowired
    private ShowSeasonEpisodeRepository showSeasonEpisodeRepository;

    @Autowired
    private EpisodeRepository episodeRepository;

    @GetMapping("/tv/seasons")
    public String getSeason(
        @RequestParam("tv") Integer id,
        @RequestParam("season") Integer season,
                            @CookieValue(value="token", required=false) String token, 
                            Model model) {
            User user = new User();
            if (token != null) {
                String username = getCurrentUsername(token);
                user = userRepository.findByUsername(username);
                if (user.getPicture() != null) {
                    user.setPicture64(Base64.getEncoder().encodeToString(user.getPicture()));
                }
            }
            model.addAttribute("user", user);
            List<ShowSeasonEpisode> seasonList = showSeasonEpisodeRepository.findAllByTvIdAndSeasonNum(id, season);
            List<Episode> episodeList = new ArrayList<Episode>();
            for (ShowSeasonEpisode s : seasonList) {
                Episode e = episodeRepository.findById(s.getEpId());
                episodeList.add(e);
            }
            model.addAttribute("episodes", episodeList);
            model.addAttribute("message", season.toString());
            model.addAttribute("episodeSize", episodeList.size());
            model.addAttribute("queryOption", new QueryOption());
            return "seasons";
    }

    @GetMapping("/tv/rated")
    public String getTvRated(@RequestParam(value="page", required=true) Integer page,
                            @CookieValue(value="token", required=false) String token, 
                            Model model) {
        User user = new User();
        if (token != null) {
            String username = getCurrentUsername(token);
            user = userRepository.findByUsername(username);
            if (user.getPicture() != null) {
                user.setPicture64(Base64.getEncoder().encodeToString(user.getPicture()));
            }
        }
        model.addAttribute("user", user);
        List<TvShow> tvList = tvRepository.findAllByOrderByAvgRatingDesc();
        model.addAttribute("size", tvList.size());
        model.addAttribute("current", page);
        model.addAttribute("queryOption", new QueryOption());
        model.addAttribute("display", tvList.subList(12*(page-1), 12*page));
        return "ratedtv";
    }

    @GetMapping("/tv/newtv")
    public String getTvNewShows(@CookieValue(value="token", required=false) String token, Model model) {
        User user = new User();
        if (token != null) {
            String username = getCurrentUsername(token);
            user = userRepository.findByUsername(username);
            if (user.getPicture() != null) {
                user.setPicture64(Base64.getEncoder().encodeToString(user.getPicture()));
            }
        }
        model.addAttribute("user", user);
        List<Episode> epList = episodeRepository.findByCurdate();
        List<TvShow> tvList = new ArrayList<TvShow>();
        for (Episode e : epList) {
            TvShow toAdd = tvRepository.findByTitle(e.getShowTitle());
            if (!tvList.contains(toAdd)) {
                tvList.add(toAdd);
            }
        }
        model.addAttribute("message", "New TV Shows Tonight");
        model.addAttribute("size", tvList.size());
        model.addAttribute("queryOption", new QueryOption());
        model.addAttribute("display", tvList);
        return "newtv";
    }

    @GetMapping("/tv/fresh")
    public String getTvFresh(@CookieValue(value="token", required=false) String token, Model model) {
        User user = new User();
        if (token != null) {
            String username = getCurrentUsername(token);
            user = userRepository.findByUsername(username);
            if (user.getPicture() != null) {
                user.setPicture64(Base64.getEncoder().encodeToString(user.getPicture()));
            }
        }
        model.addAttribute("user", user);
        List<TvShow> tvList = tvRepository.findFirst12ByOrderByAvgCriticRatingDesc();
        model.addAttribute("message", "Certified Fresh TV Shows");
        model.addAttribute("size", tvList.size());
        model.addAttribute("queryOption", new QueryOption());
        model.addAttribute("display", tvList);
        return "newtv";
    }

    @GetMapping("/tv")
    public String getTv(
        @RequestParam("id") Integer id,
                            @CookieValue(value="token", required=false) String token, 
                            Model model) {
        User user = new User();
        WatchlistTv watchlist = new WatchlistTv();
        BlacklistTv blacklist = new BlacklistTv();
        WatchlistTv watchCheck = null;
        BlacklistTv blackCheck = null;
        TvReview review = new TvReview();
        if (token != null) {
            String username = getCurrentUsername(token);
            user = userRepository.findByUsername(username);
            if (user.getPicture() != null) {
                user.setPicture64(Base64.getEncoder().encodeToString(user.getPicture()));
            }
            watchlist.setUsername(username);
            blacklist.setUsername(username);
            review.setUsername(username);
            watchCheck = watchlistTvRepository.findByUsernameAndTvId(username, id);
            blackCheck = blacklistTvRepository.findByUsernameAndTvId(username, id);
            TvReview myReview = tvReviewRepository.findByUsernameAndTvId(username, id);
            model.addAttribute("myreview", myReview);
        }
        List<TvCast> castList = tvCastRepository.findAllByTvId(id);
        model.addAttribute("cast", castList);
        model.addAttribute("castSize", castList.size());
        List<Person> personList = new ArrayList<Person>();
        for (TvCast c : castList) {
            Person p = personRepository.findById(c.getPersonId());
            personList.add(p);
        }
        model.addAttribute("person", personList);
        model.addAttribute("user", user);
        model.addAttribute("watchlist", watchlist);
        model.addAttribute("blacklist", blacklist);
        model.addAttribute("review", review);
        TvShow tv = tvRepository.findById(id);
        if (watchCheck != null) {
            tv.setWatchBool(true);
        } else {
            tv.setWatchBool(false);
        }
        if (blackCheck != null) {
            tv.setBlackBool(true);
        } else {
            tv.setBlackBool(false);
        }
        model.addAttribute("tv", tv);
        List<TvReview> reviewList = tvReviewRepository.findFirst5ByTvIdOrderByDateDesc(id);
        List<User> pics = new ArrayList<User>();
        List<Person> times = new ArrayList<Person>();
        for (TvReview tr : reviewList) {
            User u = userRepository.findByUsername(tr.getUsername());
            if (u.getPicture() != null) {
                u.setPicture64(Base64.getEncoder().encodeToString(u.getPicture()));
            }
            pics.add(u);
            Date date = new java.util.Date(tr.getDate()*1000L); 
            SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm"); 
            String formattedDate = sdf.format(date);
            Person p = new Person();
            p.setPersonName(tr.getUsername());
            p.setBio(formattedDate);
            times.add(p);
        }
        model.addAttribute("times", times);
        model.addAttribute("pics", pics);
        model.addAttribute("queryOption", new QueryOption());
        model.addAttribute("display", reviewList);
        model.addAttribute("displaySize", reviewList.size());
        return "tv";
    }

    @PostMapping(value="/tv/postreview", consumes=MediaType.APPLICATION_JSON_VALUE, produces="application/json")
    @ResponseBody
    public StringResponse postTvReview(@CookieValue(value="token", required=false) String token, @RequestBody TvReview review) {
        if (token != null) {
            String username = getCurrentUsername(token);
            if (review.getUsername().equals(username)) {
                for (String word : badWords) {
                    if (StringUtils.containsIgnoreCase(review.getBody(), word)) {
                        return new StringResponse("BAD WORD");
                    }
                }
                TvReview check = tvReviewRepository.findByUsernameAndTvId(username, review.getTvId());
                if (check == null) {
                    TvShow tv = tvRepository.findById(review.getTvId());
                    User user = userRepository.findByUsername(username);
                    int sum = review.getRating();
                    if (user.getRole().equals("Verified")) {
                        review.setRole("Verified");
                        List<TvReview> allReviews = tvReviewRepository.findAllByRoleAndTvId("Verified", review.getTvId());
                        for (TvReview rev : allReviews) {
                            sum += rev.getRating();
                        }
                        tv.setAvgGenRating((int)(20 * ((double)sum) / (allReviews.size()+1)));
                    } else {
                        review.setRole("Critic");
                        List<TvReview> allReviews = tvReviewRepository.findAllByRoleAndTvId("Critic", review.getTvId());
                        for (TvReview rev : allReviews) {
                            sum += rev.getRating();
                        }
                        tv.setAvgCriticRating((int)(20 * ((double)sum) / (allReviews.size()+1)));
                    }
                    tvReviewRepository.save(review);
                    sum = 0;
                    List<TvReview> allAvg = tvReviewRepository.findAllByTvId(review.getTvId());
                    for (TvReview inAllAvg : allAvg) {
                        sum += inAllAvg.getRating();
                    }
                    tv.setAvgRating((int)(20 * ((double)sum) / allAvg.size()));
                    tvRepository.save(tv);
                    return new StringResponse("OK");
                }
            }
        }
        return new StringResponse("ERROR");
    }

    @PostMapping(value="/tv/reportreview", consumes=MediaType.APPLICATION_JSON_VALUE, produces="application/json")
    @ResponseBody
    public StringResponse reportTvReview(@CookieValue(value="token", required=false) String token, @RequestBody ReportedReviewTv review) {
        if (token != null) {
            String username = getCurrentUsername(token);
            if (review.getUsername().equals(username)) {
                ReportedReviewTv check = reportedReviewTvRepository.findByUsernameAndTvId(username, review.getTvId());
                if (check == null) {
                    reportedReviewTvRepository.save(review);
                    return new StringResponse("OK");
                }
            }
        }
        return new StringResponse("ERROR");
    }

    @PostMapping(value="/tv/deletereview", consumes=MediaType.APPLICATION_JSON_VALUE, produces="application/json")
    @ResponseBody
    public StringResponse deleteReview(@CookieValue(value="token", required=false) String token, @RequestBody TvReview review) {
        if (token != null) {
            String username = getCurrentUsername(token);
            if (review.getUsername().equals(username)) {
                TvReview toDelete = tvReviewRepository.findByUsernameAndTvId(username, review.getTvId());
                tvReviewRepository.delete(toDelete);
                TvShow tv = tvRepository.findById(review.getTvId());
                User user = userRepository.findByUsername(username);
                int sum = 0;
                if (user.getRole().equals("Verified")) {
                    List<TvReview> allReviews = tvReviewRepository.findAllByRoleAndTvId("Verified", review.getTvId());
                    for (TvReview rev : allReviews) {
                        sum += rev.getRating();
                    }
                    tv.setAvgGenRating((int)(20 * ((double)sum) / allReviews.size()));
                } else {
                    List<TvReview> allReviews = tvReviewRepository.findAllByRoleAndTvId("Critic", review.getTvId());
                    for (TvReview rev : allReviews) {
                        sum += rev.getRating();
                    }
                    tv.setAvgCriticRating((int)(20 * ((double)sum) / allReviews.size()));
                }
                sum = 0;
                List<TvReview> allAvg = tvReviewRepository.findAllByTvId(review.getTvId());
                for (TvReview inAllAvg : allAvg) {
                    sum += inAllAvg.getRating();
                }
                tv.setAvgRating((int)(20 * ((double)sum) / allAvg.size()));
                tvRepository.save(tv);
                return new StringResponse("OK");
            }
        }
        return new StringResponse("ERROR");
    }

    @PostMapping(value="/tv/editreview", consumes=MediaType.APPLICATION_JSON_VALUE, produces="application/json")
    @ResponseBody
    public StringResponse editReview(@CookieValue(value="token", required=false) String token, @RequestBody TvReview review) {
        if (token != null) {
            String username = getCurrentUsername(token);
            if (review.getUsername().equals(username)) {
                for (String word : badWords) {
                    if (StringUtils.containsIgnoreCase(review.getBody(), word)) {
                        return new StringResponse("BAD WORD");
                    }
                }
                TvReview reviewToEdit = tvReviewRepository.findByUsernameAndTvId(username, review.getTvId());
                reviewToEdit.setBody(review.getBody());
                reviewToEdit.setDate(review.getDate());
                TvShow tv = tvRepository.findById(review.getTvId());
                int sum = 0;
                if (reviewToEdit.getRating() != review.getRating()) {
                    User user = userRepository.findByUsername(username);
                    reviewToEdit.setRating(review.getRating());
                    if (user.getRole().equals("Verified")) {
                        reviewToEdit.setRole("Verified");
                        tvReviewRepository.save(reviewToEdit);
                        List<TvReview> allReviews = tvReviewRepository.findAllByRoleAndTvId("Verified", review.getTvId());
                        for (TvReview rev : allReviews) {
                            sum += rev.getRating();
                        }
                        tv.setAvgGenRating((int)(20 * ((double)sum) / allReviews.size()));
                    } else {
                        reviewToEdit.setRole("Critic");
                        tvReviewRepository.save(reviewToEdit);
                        List<TvReview> allReviews = tvReviewRepository.findAllByRoleAndTvId("Critic", review.getTvId());
                        for (TvReview rev : allReviews) {
                            sum += rev.getRating();
                        }
                        tv.setAvgCriticRating((int)(20 * ((double)sum) / allReviews.size()));
                    }
                } else {
                    tvReviewRepository.save(reviewToEdit);
                }
                sum = 0;
                List<TvReview> allAvg = tvReviewRepository.findAllByTvId(review.getTvId());
                for (TvReview inAllAvg : allAvg) {
                    sum += inAllAvg.getRating();
                }
                tv.setAvgRating((int)(20 * ((double)sum) / allAvg.size()));
                tvRepository.save(tv);
                return new StringResponse("OK");
            }
        }
        return new StringResponse("ERROR");
    }

    private String getCurrentUsername(String token) {
        String username = Jwts.parser()
                    .setSigningKey(SECRET.getBytes())
                    .parseClaimsJws(token.replace("Bearer", ""))
                    .getBody()
                    .getSubject();
        return username;
    }
}