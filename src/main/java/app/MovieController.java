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
import java.util.Date;
import java.text.SimpleDateFormat;
import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static app.JWT.EXPIRATION_TIME;
import static app.JWT.SECRET;
import static app.Constants.badWords;

@Controller
public class MovieController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private OpeningWeekRepository openingWeekRepository;

    @Autowired
    private OpeningSoonRepository openingSoonRepository;

    @Autowired
    private MovieBoxOfficeRepository movieBoxOfficeRepository;

    @Autowired
    private WatchlistRepository watchlistRepository;

    @Autowired
    private BlacklistRepository blacklistRepository;

    @Autowired
    private MovieReviewRepository movieReviewRepository;

    @Autowired
    private ReportedReviewRepository reportedReviewRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private CastRepository castRepository;

    @GetMapping("/movies/rated")
    public String getMoviesRated(@RequestParam(value="page", required=true) Integer page,
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
        List<Movie> movieList = movieRepository.findAllByOrderByAvgRatingDesc();
        model.addAttribute("size", movieList.size());
        model.addAttribute("current", page);
        model.addAttribute("queryOption", new QueryOption());
        model.addAttribute("display", movieList.subList(12*(page-1), 12*page));
        return "rated";
    }

    @GetMapping("/movies/opening")
    public String getMoviesOpening(@CookieValue(value="token", required=false) String token, Model model) {
        User user = new User();
        if (token != null) {
            String username = getCurrentUsername(token);
            user = userRepository.findByUsername(username);
            if (user.getPicture() != null) {
                user.setPicture64(Base64.getEncoder().encodeToString(user.getPicture()));
            }
        }
        model.addAttribute("user", user);
        List<OpeningWeek> openList = openingWeekRepository.findAll();
        List<Movie> movieList = new ArrayList<Movie>();
        for (OpeningWeek o : openList) {
            Movie m = movieRepository.findById(o.getMovieId());
            movieList.add(m);
        }
        model.addAttribute("size", movieList.size());
        model.addAttribute("queryOption", new QueryOption());
        model.addAttribute("display", movieList);
        model.addAttribute("message", "Opening This Week");
        return "opening";
    }

    @GetMapping("/movies/comingsoon")
    public String getMoviesSoon(@CookieValue(value="token", required=false) String token, Model model) {
        User user = new User();
        if (token != null) {
            String username = getCurrentUsername(token);
            user = userRepository.findByUsername(username);
            if (user.getPicture() != null) {
                user.setPicture64(Base64.getEncoder().encodeToString(user.getPicture()));
            }
        }
        model.addAttribute("user", user);
        List<OpeningSoon> openList = openingSoonRepository.findAll();
        List<Movie> movieList = new ArrayList<Movie>();
        for (OpeningSoon o : openList) {
            Movie m = movieRepository.findById(o.getMovieId());
            movieList.add(m);
        }
        model.addAttribute("size", movieList.size());
        model.addAttribute("queryOption", new QueryOption());
        model.addAttribute("display", movieList);
        model.addAttribute("message", "Coming Soon To Theaters");
        return "opening";
    }

    @GetMapping("/movies/boxoffice")
    public String getMoviesBoxOffice(@CookieValue(value="token", required=false) String token, Model model) {
        User user = new User();
        if (token != null) {
            String username = getCurrentUsername(token);
            user = userRepository.findByUsername(username);
            if (user.getPicture() != null) {
                user.setPicture64(Base64.getEncoder().encodeToString(user.getPicture()));
            }
        }
        model.addAttribute("user", user);
        List<MovieBoxOffice> boxList = movieBoxOfficeRepository.findAll();
        List<Movie> movieList = new ArrayList<Movie>();
        Collections.sort(boxList, new Comparator<MovieBoxOffice>() {
            public int compare(MovieBoxOffice one, MovieBoxOffice two) {
                return two.getWeeklyBudget().compareTo(one.getWeeklyBudget());
            }
        });
        for (MovieBoxOffice b : boxList) {
            Movie m = movieRepository.findById(b.getMovieId());
            movieList.add(m);
        }
        model.addAttribute("size", movieList.size());
        model.addAttribute("queryOption", new QueryOption());
        model.addAttribute("display", movieList);
        model.addAttribute("budget", boxList);
        return "boxoffice";
    }

    @GetMapping("/movies/awardwinners")
    public String getMoviesOscar(@RequestParam(value="year", required=false) Integer year,
                                @RequestParam(value="page", required=true) Integer page,
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
        model.addAttribute("current", page);
        model.addAttribute("queryOption", new QueryOption());
        if (year == null) {
            List<Movie> movieList = movieRepository.findAllByOscarBoolOrderByReleaseDateDesc(true);
            model.addAttribute("size", movieList.size());
            model.addAttribute("display", movieList.subList(12*(page-1), 12*page));
            model.addAttribute("pagination", true);
        } else {
            List<Movie> movieList = movieRepository.findAllByOscarBoolAndYearOrderByReleaseDateDesc(year, true);
            model.addAttribute("size", movieList.size());
            model.addAttribute("display", movieList);
            model.addAttribute("pagination", false);
        }
        return "awardwinners";
    }

    @GetMapping("/movie")
    public String getMovie(
        @RequestParam("id") Integer id,
                            @CookieValue(value="token", required=false) String token, 
                            Model model) {
        User user = new User();
        Watchlist watchlist = new Watchlist();
        Blacklist blacklist = new Blacklist();
        Watchlist watchCheck = null;
        Blacklist blackCheck = null;
        MovieReview review = new MovieReview();
        if (token != null) {
            String username = getCurrentUsername(token);
            user = userRepository.findByUsername(username);
            if (user.getPicture() != null) {
                user.setPicture64(Base64.getEncoder().encodeToString(user.getPicture()));
            }
            watchlist.setUsername(username);
            blacklist.setUsername(username);
            review.setUsername(username);
            watchCheck = watchlistRepository.findByUsernameAndMovieId(username, id);
            blackCheck = blacklistRepository.findByUsernameAndMovieId(username, id);
            MovieReview myReview = movieReviewRepository.findByUsernameAndMovieId(username, id);
            model.addAttribute("myreview", myReview);
        }
        List<Cast> castList = castRepository.findByMovieId(id);
        model.addAttribute("cast", castList);
        model.addAttribute("castSize", castList.size());
        List<Person> personList = new ArrayList<Person>();
        for (Cast c : castList) {
            Person p = personRepository.findById(c.getPersonId());
            personList.add(p);
        }
        model.addAttribute("person", personList);
        model.addAttribute("user", user);
        model.addAttribute("watchlist", watchlist);
        model.addAttribute("blacklist", blacklist);
        model.addAttribute("review", review);
        Movie movie = movieRepository.findById(id);
        if (watchCheck != null) {
            movie.setWatchBool(true);
        } else {
            movie.setWatchBool(false);
        }
        if (blackCheck != null) {
            movie.setBlackBool(true);
        } else {
            movie.setBlackBool(false);
        }
        model.addAttribute("movie", movie);
        List<MovieReview> reviewList = movieReviewRepository.findFirst5ByMovieIdOrderByDateDesc(id);
        List<User> pics = new ArrayList<User>();
        List<Person> times = new ArrayList<Person>();
        for (MovieReview mr : reviewList) {
            User u = userRepository.findByUsername(mr.getUsername());
            if (u.getPicture() != null) {
                u.setPicture64(Base64.getEncoder().encodeToString(u.getPicture()));
            }
            pics.add(u);
            Date date = new java.util.Date(mr.getDate()*1000L); 
            SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm"); 
            String formattedDate = sdf.format(date);
            Person p = new Person();
            p.setPersonName(mr.getUsername());
            p.setBio(formattedDate);
            times.add(p);
        }
        model.addAttribute("times", times);
        model.addAttribute("pics", pics);
        model.addAttribute("queryOption", new QueryOption());
        model.addAttribute("display", reviewList);
        model.addAttribute("displaySize", reviewList.size());
        return "movie";
    }

    @PostMapping(value="/movie/postreview", consumes=MediaType.APPLICATION_JSON_VALUE, produces="application/json")
    @ResponseBody
    public StringResponse postReview(@CookieValue(value="token", required=false) String token, @RequestBody MovieReview review) {
        if (token != null) {
            String username = getCurrentUsername(token);
            if (review.getUsername().equals(username)) {
                for (String word : badWords) {
                    if (StringUtils.containsIgnoreCase(review.getBody(), word)) {
                        return new StringResponse("BAD WORD");
                    }
                }
                MovieReview check = movieReviewRepository.findByUsernameAndMovieId(username, review.getMovieId());
                if (check == null) {
                    Movie movie = movieRepository.findById(review.getMovieId());
                    User user = userRepository.findByUsername(username);
                    int sum = review.getRating();
                    if (user.getRole().equals("Verified")) {
                        review.setRole("Verified");
                        List<MovieReview> allReviews = movieReviewRepository.findAllByRoleAndMovieId("Verified", review.getMovieId());
                        for (MovieReview rev : allReviews) {
                            sum += rev.getRating();
                        }
                        movie.setAvgGenRating((int)(20 * ((double)sum) / (allReviews.size()+1)));
                    } else {
                        review.setRole("Critic");
                        List<MovieReview> allReviews = movieReviewRepository.findAllByRoleAndMovieId("Critic", review.getMovieId());
                        for (MovieReview rev : allReviews) {
                            sum += rev.getRating();
                        }
                        movie.setAvgCriticRating((int)(20 * ((double)sum) / (allReviews.size()+1)));
                    }
                    movieReviewRepository.save(review);
                    sum = 0;
                    List<MovieReview> allAvg = movieReviewRepository.findAllByMovieId(review.getMovieId());
                    for (MovieReview inAllAvg : allAvg) {
                        sum += inAllAvg.getRating();
                    }
                    movie.setAvgRating((int)(20 * ((double)sum) / allAvg.size()));
                    movieRepository.save(movie);
                    return new StringResponse("OK");
                }
            }
        }
        return new StringResponse("ERROR");
    }

    @PostMapping(value="/movie/reportreview", consumes=MediaType.APPLICATION_JSON_VALUE, produces="application/json")
    @ResponseBody
    public StringResponse reportReview(@CookieValue(value="token", required=false) String token, @RequestBody ReportedReview review) {
        if (token != null) {
            String username = getCurrentUsername(token);
            if (review.getUsername().equals(username)) {
                ReportedReview check = reportedReviewRepository.findByUsernameAndMovieId(username, review.getMovieId());
                if (check == null) {
                    reportedReviewRepository.save(review);
                    return new StringResponse("OK");
                }
            }
        }
        return new StringResponse("ERROR");
    }

    @PostMapping(value="/movie/deletereview", consumes=MediaType.APPLICATION_JSON_VALUE, produces="application/json")
    @ResponseBody
    public StringResponse deleteReview(@CookieValue(value="token", required=false) String token, @RequestBody MovieReview review) {
        if (token != null) {
            String username = getCurrentUsername(token);
            if (review.getUsername().equals(username)) {
                MovieReview toDelete = movieReviewRepository.findByUsernameAndMovieId(username, review.getMovieId());
                movieReviewRepository.delete(toDelete);
                Movie movie = movieRepository.findById(review.getMovieId());
                User user = userRepository.findByUsername(username);
                int sum = 0;
                if (user.getRole().equals("Verified")) {
                    List<MovieReview> allReviews = movieReviewRepository.findAllByRoleAndMovieId("Verified", review.getMovieId());
                    for (MovieReview rev : allReviews) {
                        sum += rev.getRating();
                    }
                    movie.setAvgGenRating((int)(20 * ((double)sum) / allReviews.size()));
                } else {
                    List<MovieReview> allReviews = movieReviewRepository.findAllByRoleAndMovieId("Critic", review.getMovieId());
                    for (MovieReview rev : allReviews) {
                        sum += rev.getRating();
                    }
                    movie.setAvgCriticRating((int)(20 * ((double)sum) / allReviews.size()));
                }
                sum = 0;
                List<MovieReview> allAvg = movieReviewRepository.findAllByMovieId(review.getMovieId());
                for (MovieReview inAllAvg : allAvg) {
                    sum += inAllAvg.getRating();
                }
                movie.setAvgRating((int)(20 * ((double)sum) / allAvg.size()));
                movieRepository.save(movie);
                return new StringResponse("OK");
            }
        }
        return new StringResponse("ERROR");
    }

    @PostMapping(value="/movie/editreview", consumes=MediaType.APPLICATION_JSON_VALUE, produces="application/json")
    @ResponseBody
    public StringResponse editReview(@CookieValue(value="token", required=false) String token, @RequestBody MovieReview review) {
        if (token != null) {
            String username = getCurrentUsername(token);
            if (review.getUsername().equals(username)) {
                for (String word : badWords) {
                    if (StringUtils.containsIgnoreCase(review.getBody(), word)) {
                        return new StringResponse("BAD WORD");
                    }
                }
                MovieReview reviewToEdit = movieReviewRepository.findByUsernameAndMovieId(username, review.getMovieId());
                reviewToEdit.setBody(review.getBody());
                reviewToEdit.setDate(review.getDate());
                Movie movie = movieRepository.findById(review.getMovieId());
                int sum = 0;
                if (reviewToEdit.getRating() != review.getRating()) {
                    User user = userRepository.findByUsername(username);
                    reviewToEdit.setRating(review.getRating());
                    if (user.getRole().equals("Verified")) {
                        reviewToEdit.setRole("Verified");
                        movieReviewRepository.save(reviewToEdit);
                        List<MovieReview> allReviews = movieReviewRepository.findAllByRoleAndMovieId("Verified", review.getMovieId());
                        for (MovieReview rev : allReviews) {
                            sum += rev.getRating();
                        }
                        movie.setAvgGenRating((int)(20 * ((double)sum) / allReviews.size()));
                    } else {
                        reviewToEdit.setRole("Critic");
                        movieReviewRepository.save(reviewToEdit);
                        List<MovieReview> allReviews = movieReviewRepository.findAllByRoleAndMovieId("Critic", review.getMovieId());
                        for (MovieReview rev : allReviews) {
                            sum += rev.getRating();
                        }
                        movie.setAvgCriticRating((int)(20 * ((double)sum) / allReviews.size()));
                    }
                } else {
                    movieReviewRepository.save(reviewToEdit);
                }
                sum = 0;
                List<MovieReview> allAvg = movieReviewRepository.findAllByMovieId(review.getMovieId());
                for (MovieReview inAllAvg : allAvg) {
                    sum += inAllAvg.getRating();
                }
                movie.setAvgRating((int)(20 * ((double)sum) / allAvg.size()));
                movieRepository.save(movie);
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