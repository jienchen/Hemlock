package app;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.Cookie;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.beans.factory.annotation.Autowired;
import javax.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.http.MediaType;
import java.util.Random;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Base64;
import java.sql.Blob;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.springframework.web.multipart.MultipartFile;
import java.text.SimpleDateFormat;
import java.util.*;

import static app.JWT.EXPIRATION_TIME;
import static app.JWT.SECRET;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WatchlistRepository watchlistRepository;

    @Autowired
    private BlacklistRepository blacklistRepository;

    @Autowired
    private WatchlistTvRepository watchlistTvRepository;

    @Autowired
    private BlacklistTvRepository blacklistTvRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieReviewRepository movieReviewRepository;

    @Autowired
    private TvShowRepository tvRepository;

    @Autowired
    private TvReviewRepository tvReviewRepository;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private CriticApplicationsRepository criticApplicationsRepository;

    @Autowired
    private OpeningWeekRepository openingWeekRepository;

    @Autowired
    private OpeningSoonRepository openingSoonRepository;

    @Autowired
    private MovieBoxOfficeRepository movieBoxOfficeRepository;

    @Autowired
    private JavaMailSender sender;

    @GetMapping("/register")
    public String getRegister(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("message", new Message());
        return "register";
    }

    @GetMapping("/verify")
    public String getVerify(@RequestParam(value="email", required=false) String email, 
                            @RequestParam(value="key", required=false) String key, 
                            Model model) {
        User user = new User();
        user.setEmail(email);
        user.setKey(key);
        model.addAttribute("user", user);
        model.addAttribute("message", new Message());
        return "verify";
    }

    @GetMapping("/reset")
    public String getReset(@RequestParam(value="email", required=false) String email, 
                            @RequestParam(value="token", required=false) String token,
                            Model model) {
        User user = new User();
        user.setEmail(email);
        user.setResetToken(token);
        model.addAttribute("user", user);
        model.addAttribute("message", new Message());
        return "reset";
    }

    @PostMapping("/register")
    public String postRegister(HttpServletRequest req, Model model, @ModelAttribute User user) {
        model.addAttribute("user", new User());
        Message message = new Message();
        User checkEmail = userRepository.findByEmail(user.getEmail());
        if (checkEmail != null) {
            message.setMessage("Email is taken!");
            model.addAttribute("message", message);
            return "register";
        }
        User checkUsername = userRepository.findByUsername(user.getUsername());
        if (checkUsername != null) {
            message.setMessage("Username is taken!");
            model.addAttribute("message", message);
            return "register";
        }
        user.setKey(getKey());
        user.setPassword(hash(user.getPassword()));
        user.setVerified(false);
        user.setRole("Unverified");
        userRepository.save(user);
        String link = "http://" + req.getServerName() + ":" + req.getServerPort() + 
                        "/verify?email=" + user.getEmail() + "&key=" + user.getKey();
        try {
            sendEmail(user.getEmail(), link);
            message.setMessage("Registration Successful");
            model.addAttribute("message", message);
            return "register";
        } catch (Exception ex) {
            message.setMessage("Registration Unsuccessful");
            model.addAttribute("message", message);
            return "register";
        }
    }

    @PostMapping(value="/reverify")
    public String reverify(HttpServletRequest req, @RequestBody String username, Model model) {
        User user  = userRepository.findByUsername(username);
        if (user != null) {
            String link = "http://" + req.getServerName() + ":" + req.getServerPort() + 
                        "/verify?email=" + user.getEmail() + "&key=" + user.getKey();
            try {
                sendEmail(user.getEmail(), link);
            } catch (Exception ex) {
            }
        }
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
        List<OpeningWeek> openList = openingWeekRepository.findAll();
        List<Movie> movieList2 = new ArrayList<Movie>();
        for (OpeningWeek o : openList) {
            Movie m = movieRepository.findById(o.getMovieId());
            movieList2.add(m);
        }
        List<OpeningSoon> openList2 = openingSoonRepository.findAll();
        List<Movie> movieList3 = new ArrayList<Movie>();
        for (OpeningSoon o : openList2) {
            Movie m = movieRepository.findById(o.getMovieId());
            movieList3.add(m);
        }
        model.addAttribute("boxoffice", movieList.subList(0,4));
        model.addAttribute("openingweek", movieList2.subList(0,4));
        model.addAttribute("openingsoon", movieList3.subList(0,4));
        model.addAttribute("user", new User());
        model.addAttribute("queryOption", new QueryOption());
        return "index";
    }

    @PostMapping(value="/changeemail", consumes=MediaType.APPLICATION_JSON_VALUE, produces="application/json")
    @ResponseBody
    public StringResponse changeEmail(
                                @CookieValue(value="token", required=false) String token, 
                                @RequestBody User user) {
        if (token != null) {
            String username = getCurrentUsername(token);
            if (username.equals(user.getUsername())) {
                User toVerify  = userRepository.findByEmail(user.getEmail());
                User check = userRepository.findByEmail(user.getResetToken());
                if (toVerify != null && check == null) {
                    if (toVerify.getPassword().equals(hash(user.getPassword()))) {
                        toVerify.setEmail(user.getResetToken());
                        userRepository.save(toVerify);
                        return new StringResponse("OK");
                    }
                }
            }
        }
        return new StringResponse("ERROR");
    }

    @PostMapping(value="/changeusername", consumes=MediaType.APPLICATION_JSON_VALUE, produces="application/json")
    @ResponseBody
    public StringResponse changeUsername(
                                HttpServletResponse res,
                                @CookieValue(value="token", required=false) String token, 
                                @RequestBody User user) {
        if (token != null) {
            String username = getCurrentUsername(token);
            if (username.equals(user.getUsername())) {
                User toVerify  = userRepository.findByUsername(username);
                User check = userRepository.findByUsername(user.getResetToken());
                if (toVerify != null && check == null) {
                    if (toVerify.getPassword().equals(hash(user.getPassword()))) {
                        toVerify.setUsername(user.getResetToken());
                        userRepository.save(toVerify);
                        List<MovieReview> reviewChange = movieReviewRepository.findAllByUsername(username);
                        for (MovieReview review : reviewChange) {
                            review.setUsername(user.getResetToken());
                            movieReviewRepository.save(review);
                        }
                        List<TvReview> reviewTvChange = tvReviewRepository.findAllByUsername(username);
                        for (TvReview review : reviewTvChange) {
                            review.setUsername(user.getResetToken());
                            tvReviewRepository.save(review);
                        }
                        List<Watchlist> watchlistChange = watchlistRepository.findAllByUsername(username);
                        for (Watchlist w : watchlistChange) {
                            w.setUsername(user.getResetToken());
                            watchlistRepository.save(w);
                        }
                        List<Blacklist> blacklistChange = blacklistRepository.findAllByUsername(username);
                        for (Blacklist b : blacklistChange) {
                            b.setUsername(user.getResetToken());
                            blacklistRepository.save(b);
                        }
                        List<WatchlistTv> watchlistTvChange = watchlistTvRepository.findAllByUsername(username);
                        for (WatchlistTv w : watchlistTvChange) {
                            w.setUsername(user.getResetToken());
                            watchlistTvRepository.save(w);
                        }
                        List<BlacklistTv> blacklistTvChange = blacklistTvRepository.findAllByUsername(username);
                        for (BlacklistTv b : blacklistTvChange) {
                            b.setUsername(user.getResetToken());
                            blacklistTvRepository.save(b);
                        }
                        List<Follow> followChange = followRepository.findAllByUsername(username);
                        for (Follow f : followChange) {
                            f.setUsername(user.getResetToken());
                            followRepository.save(f);
                        }
                        followChange = followRepository.findAllByFollow(username);
                        for (Follow f : followChange) {
                            f.setFollow(user.getResetToken());
                            followRepository.save(f);
                        }
                        String newToken = Jwts.builder()
                            .setSubject(user.getResetToken())
                            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                            .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
                            .compact();
                        Cookie jwt = new Cookie("token", newToken);
                        jwt.setMaxAge(86400);
                        res.addCookie(jwt);
                        return new StringResponse("OK");
                    }
                }
            }
        }
        return new StringResponse("ERROR");
    }

    @PostMapping(value="/changepassword", consumes=MediaType.APPLICATION_JSON_VALUE, produces="application/json")
    @ResponseBody
    public StringResponse changePassword(
                                @CookieValue(value="token", required=false) String token, 
                                @RequestBody User user) {
        if (token != null) {
            String username = getCurrentUsername(token);
            if (username.equals(user.getUsername())) {
                User toVerify  = userRepository.findByUsername(username);
                if (toVerify != null) {
                    if (toVerify.getPassword().equals(hash(user.getPassword()))) {
                        toVerify.setPassword(hash(user.getResetToken()));
                        userRepository.save(toVerify);
                        return new StringResponse("OK");
                    }
                }
            }
        }
        return new StringResponse("ERROR");
    }

    @PostMapping(value="/changebio", consumes=MediaType.APPLICATION_JSON_VALUE, produces="application/json")
    @ResponseBody
    public StringResponse changeBio(
                                @CookieValue(value="token", required=false) String token, 
                                @RequestBody User user) {
        if (token != null) {
            String username = getCurrentUsername(token);
            if (username.equals(user.getUsername())) {
                User toVerify  = userRepository.findByUsername(username);
                if (toVerify != null) {
                    toVerify.setBio(user.getResetToken());
                    userRepository.save(toVerify);
                    return new StringResponse("OK");
                }
            }
        }
        return new StringResponse("ERROR");
    }

    @PostMapping(value="/forgotpassword", consumes=MediaType.APPLICATION_JSON_VALUE, produces="application/json")
    @ResponseBody
    public StringResponse forgotPassword(HttpServletRequest req, @RequestBody User user) {
        User toResend = userRepository.findByEmail(user.getEmail());
        if (toResend != null) {
            toResend.setResetToken(getKey());
            userRepository.save(toResend);
            String link = "http://" + req.getServerName() + ":" + req.getServerPort() + 
                        "/reset?email=" + user.getEmail() + "&token=" + toResend.getResetToken();
            try {
                MimeMessage message = sender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message);
                helper.setTo(user.getEmail());
                helper.setText("Please use the following link to reset your password: \n" + link);
                helper.setSubject("Password Reset");
                sender.send(message);
                return new StringResponse("OK");
            } catch (Exception ex) {
                return new StringResponse("ERROR");
            }
        } 
        return new StringResponse("ERROR");
    }

    @PostMapping(value="/deleteaccount")
    public String deleteAccount(HttpServletResponse res, 
                                @CookieValue(value="token", required=false) String token, 
                                Model model) {
        if (token != null) {
            String username = getCurrentUsername(token);
            User toDelete = userRepository.findByUsername(username);
            userRepository.delete(toDelete);
            movieReviewRepository.deleteByUsername(username);
            tvReviewRepository.deleteByUsername(username);
            watchlistRepository.deleteByUsername(username);
            blacklistRepository.deleteByUsername(username);
            watchlistTvRepository.deleteByUsername(username);
            blacklistTvRepository.deleteByUsername(username);
            followRepository.deleteByUsername(username);
            followRepository.deleteByFollow(username);
            criticApplicationsRepository.deleteByUsername(username);
            Cookie remove = new Cookie("token", null);
            remove.setMaxAge(0);
            res.addCookie(remove);
        }
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
        List<OpeningWeek> openList = openingWeekRepository.findAll();
        List<Movie> movieList2 = new ArrayList<Movie>();
        for (OpeningWeek o : openList) {
            Movie m = movieRepository.findById(o.getMovieId());
            movieList2.add(m);
        }
        List<OpeningSoon> openList2 = openingSoonRepository.findAll();
        List<Movie> movieList3 = new ArrayList<Movie>();
        for (OpeningSoon o : openList2) {
            Movie m = movieRepository.findById(o.getMovieId());
            movieList3.add(m);
        }
        model.addAttribute("boxoffice", movieList.subList(0,4));
        model.addAttribute("openingweek", movieList2.subList(0,4));
        model.addAttribute("openingsoon", movieList3.subList(0,4));
        model.addAttribute("user", new User());
        model.addAttribute("queryOption", new QueryOption());
        return "index";
    }

    @PostMapping("/verify")
    public String postVerify(Model model, @ModelAttribute User user) {
        model.addAttribute("user", new User());
        Message message = new Message();
        User toVerify = userRepository.findByEmail(user.getEmail());
        if (toVerify != null) {
            if (toVerify.getKey().equals(user.getKey())) {
                toVerify.setVerified(true);
                toVerify.setRole("Verified");
                userRepository.save(toVerify);
                message.setMessage("Verification Successful");
                model.addAttribute("message", message);
                return "verify";
            }
        }
        message.setMessage("Verification Unsuccessful");
        model.addAttribute("message", message);
        return "verify";
    }

    @PostMapping("/reset")
    public String postReset(Model model, @ModelAttribute User user) {
        model.addAttribute("user", new User());
        Message message = new Message();
        User toReset = userRepository.findByEmail(user.getEmail());
        if (toReset != null) {
            if (toReset.getResetToken().equals(user.getResetToken())) {
                toReset.setPassword(user.getPassword());
                toReset.setResetToken("");
                userRepository.save(toReset);
                message.setMessage("Password has been Reset");
                model.addAttribute("message", message);
                return "reset";
            }
        }
        message.setMessage("Failed to Reset Password");
        model.addAttribute("message", message);
        return "reset";
    }

    @PostMapping("/login")
    public String login(HttpServletResponse res, @ModelAttribute User user, Model model) {
        model.addAttribute("queryOption", new QueryOption());
        User toLogin = userRepository.findByUsername(user.getUsername());
        model.addAttribute("almost", false);
        model.addAttribute("fail", false);
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
        List<OpeningWeek> openList = openingWeekRepository.findAll();
        List<Movie> movieList2 = new ArrayList<Movie>();
        for (OpeningWeek o : openList) {
            Movie m = movieRepository.findById(o.getMovieId());
            movieList2.add(m);
        }
        List<OpeningSoon> openList2 = openingSoonRepository.findAll();
        List<Movie> movieList3 = new ArrayList<Movie>();
        for (OpeningSoon o : openList2) {
            Movie m = movieRepository.findById(o.getMovieId());
            movieList3.add(m);
        }
        model.addAttribute("boxoffice", movieList.subList(0,4));
        model.addAttribute("openingweek", movieList2.subList(0,4));
        model.addAttribute("openingsoon", movieList3.subList(0,4));
        if (toLogin != null) {
            if (toLogin.getPassword().equals(hash(user.getPassword()))) {
                if (toLogin.getVerified()) {
                    String token = Jwts.builder()
                        .setSubject(toLogin.getUsername())
                        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                        .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
                        .compact();
                    Cookie jwt = new Cookie("token", token);
                    jwt.setMaxAge(86400);
                    res.addCookie(jwt);
                    if (toLogin.getPicture() != null) {
                        toLogin.setPicture64(Base64.getEncoder().encodeToString(toLogin.getPicture()));
                    }
                    model.addAttribute("user", toLogin);
                    return "index";
                }
                model.addAttribute("almost", true);
                model.addAttribute("u", toLogin.getUsername());
            }
        }
        model.addAttribute("user", new User());
        model.addAttribute("fail", true);
        return "index";
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse res, @CookieValue(value="token", required=false) String token, Model model) {
        if (token != null) {
            Cookie remove = new Cookie("token", null);
            remove.setMaxAge(0);
            res.addCookie(remove);
        }
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
        List<OpeningWeek> openList = openingWeekRepository.findAll();
        List<Movie> movieList2 = new ArrayList<Movie>();
        for (OpeningWeek o : openList) {
            Movie m = movieRepository.findById(o.getMovieId());
            movieList2.add(m);
        }
        List<OpeningSoon> openList2 = openingSoonRepository.findAll();
        List<Movie> movieList3 = new ArrayList<Movie>();
        for (OpeningSoon o : openList2) {
            Movie m = movieRepository.findById(o.getMovieId());
            movieList3.add(m);
        }
        model.addAttribute("boxoffice", movieList.subList(0,4));
        model.addAttribute("openingweek", movieList2.subList(0,4));
        model.addAttribute("openingsoon", movieList3.subList(0,4));
        model.addAttribute("user", new User());
        model.addAttribute("queryOption", new QueryOption());
        return "index";
    }

    @PostMapping(value="/user/upload", produces="application/json")
    @ResponseBody
    public StringResponse upload(HttpServletRequest req,
                                @CookieValue(value="token", required=false) String token, 
                                @RequestParam("file") MultipartFile multipartFile) {
        if (token != null) {
            String username = getCurrentUsername(token);
            User user = userRepository.findByUsername(username);
            if (user != null) {
                try {
                    byte[] bytes = multipartFile.getBytes();
                    user.setPicture(bytes);
                    userRepository.save(user);
                    user.setPicture64(Base64.getEncoder().encodeToString(user.getPicture()));
                    return new StringResponse("OK");
                } catch (Exception e) {
                    return new StringResponse("ERROR");
                }
            }
        }
        return new StringResponse("ERROR");
    }

    @PostMapping(value="/user/addwatchlist", consumes=MediaType.APPLICATION_JSON_VALUE, produces="application/json")
    @ResponseBody
    public StringResponse addToWatchlist(@CookieValue(value="token", required=false) String token, @RequestBody Watchlist watchlist) {
        if (token != null) {
            String username = getCurrentUsername(token);
            if (watchlist.getUsername().equals(username)) {
                watchlistRepository.save(watchlist);
                return new StringResponse("OK");
            }
        }
        return new StringResponse("ERROR");
    }

    @PostMapping(value="/user/removewatchlist", consumes=MediaType.APPLICATION_JSON_VALUE, produces="application/json")
    @ResponseBody
    public StringResponse removeFromWatchlist(@CookieValue(value="token", required=false) String token, @RequestBody Watchlist watchlist) {
        if (token != null) {
            String username = getCurrentUsername(token);
            if (watchlist.getUsername().equals(username)) {
                watchlistRepository.delete(watchlist);
                return new StringResponse("OK");
            }
        }
        return new StringResponse("ERROR");
    }

    @PostMapping(value="/user/addblacklist", consumes=MediaType.APPLICATION_JSON_VALUE, produces="application/json")
    @ResponseBody
    public StringResponse addToBlacklist(@CookieValue(value="token", required=false) String token, @RequestBody Blacklist blacklist) {
        if (token != null) {
            String username = getCurrentUsername(token);
            if (blacklist.getUsername().equals(username)) {
                blacklistRepository.save(blacklist);
                return new StringResponse("OK");
            }
        }
        return new StringResponse("ERROR");
    }

    @PostMapping(value="/user/removeblacklist", consumes=MediaType.APPLICATION_JSON_VALUE, produces="application/json")
    @ResponseBody
    public StringResponse removeFromBlacklist(@CookieValue(value="token", required=false) String token, @RequestBody Blacklist blacklist) {
        if (token != null) {
            String username = getCurrentUsername(token);
            if (blacklist.getUsername().equals(username)) {
                blacklistRepository.delete(blacklist);
                return new StringResponse("OK");
            }
        }
        return new StringResponse("ERROR");
    }

    @PostMapping(value="/user/addwatchlisttv", consumes=MediaType.APPLICATION_JSON_VALUE, produces="application/json")
    @ResponseBody
    public StringResponse addToWatchlistTv(@CookieValue(value="token", required=false) String token, @RequestBody WatchlistTv watchlist) {
        if (token != null) {
            String username = getCurrentUsername(token);
            if (watchlist.getUsername().equals(username)) {
                watchlistTvRepository.save(watchlist);
                return new StringResponse("OK");
            }
        }
        return new StringResponse("ERROR");
    }

    @PostMapping(value="/user/removewatchlisttv", consumes=MediaType.APPLICATION_JSON_VALUE, produces="application/json")
    @ResponseBody
    public StringResponse removeFromWatchlistTv(@CookieValue(value="token", required=false) String token, @RequestBody WatchlistTv watchlist) {
        if (token != null) {
            String username = getCurrentUsername(token);
            if (watchlist.getUsername().equals(username)) {
                watchlistTvRepository.delete(watchlist);
                return new StringResponse("OK");
            }
        }
        return new StringResponse("ERROR");
    }

    @PostMapping(value="/user/addblacklisttv", consumes=MediaType.APPLICATION_JSON_VALUE, produces="application/json")
    @ResponseBody
    public StringResponse addToBlacklistTv(@CookieValue(value="token", required=false) String token, @RequestBody BlacklistTv blacklist) {
        if (token != null) {
            String username = getCurrentUsername(token);
            if (blacklist.getUsername().equals(username)) {
                blacklistTvRepository.save(blacklist);
                return new StringResponse("OK");
            }
        }
        return new StringResponse("ERROR");
    }

    @PostMapping(value="/user/removeblacklisttv", consumes=MediaType.APPLICATION_JSON_VALUE, produces="application/json")
    @ResponseBody
    public StringResponse removeFromBlacklistTv(@CookieValue(value="token", required=false) String token, @RequestBody BlacklistTv blacklist) {
        if (token != null) {
            String username = getCurrentUsername(token);
            if (blacklist.getUsername().equals(username)) {
                blacklistTvRepository.delete(blacklist);
                return new StringResponse("OK");
            }
        }
        return new StringResponse("ERROR");
    }

    @GetMapping("/user")
    public String getUser(
        @RequestParam("username") String username, @CookieValue(value="token", required=false) String token, Model model) {
        User user = userRepository.findByUsername(username);
        if (user.getPicture() != null) {
           user.setPicture64(Base64.getEncoder().encodeToString(user.getPicture()));
        }
        Message message = new Message();
        Follow follow = new Follow();
        List<Watchlist> watchlist = watchlistRepository.findAllByUsername(username);
        List<Movie> watchlistMovies = new ArrayList<Movie>();
        for (Watchlist w : watchlist) {
            Movie movie = movieRepository.findById(w.getMovieId());
            watchlistMovies.add(movie);
        }
        List<Blacklist> blacklist = blacklistRepository.findAllByUsername(username);
        List<Movie> blacklistMovies = new ArrayList<Movie>();
        for (Blacklist b : blacklist) {
            Movie movie = movieRepository.findById(b.getMovieId());
            blacklistMovies.add(movie);
        }
        List<WatchlistTv> watchlistTv = watchlistTvRepository.findAllByUsername(username);
        List<TvShow> watchlistTvShows = new ArrayList<TvShow>();
        for (WatchlistTv w : watchlistTv) {
            TvShow tv = tvRepository.findById(w.getTvId());
            watchlistTvShows.add(tv);
        }
        List<BlacklistTv> blacklistTv = blacklistTvRepository.findAllByUsername(username);
        List<TvShow> blacklistTvShows = new ArrayList<TvShow>();
        for (BlacklistTv b : blacklistTv) {
            TvShow tv = tvRepository.findById(b.getTvId());
            blacklistTvShows.add(tv);
        }
        List<MovieReview> reviewlist = movieReviewRepository.findFirst5ByUsernameOrderByDateDesc(username);
        List<TvReview> reviewlistTv = tvReviewRepository.findFirst5ByUsernameOrderByDateDesc(username);
        Long count = followRepository.countByFollow(username);
        List<Follow> followerList = followRepository.findAllByFollow(username);
        List<User> followerList2 = new ArrayList<User>();
        for (Follow f : followerList) {
            User u = userRepository.findByUsername(f.getUsername());
            if (u.getPicture() != null) {
                u.setPicture64(Base64.getEncoder().encodeToString(u.getPicture()));
            }
            followerList2.add(u);
        }
        List<Follow> followingList = followRepository.findAllByUsername(username);
        List<User> followingList2 = new ArrayList<User>();
        for (Follow f : followingList) {
            User u = userRepository.findByUsername(f.getFollow());
            if (u.getPicture() != null) {
                u.setPicture64(Base64.getEncoder().encodeToString(u.getPicture()));
            }
            followingList2.add(u);
        }
        message.setMessage(count.toString());
        model.addAttribute("followers", followerList2);
        model.addAttribute("following", followingList2);
        model.addAttribute("followerSize", followerList2.size());
        model.addAttribute("followingSize", followingList2.size());
        model.addAttribute("user", user);
        model.addAttribute("watchlist", watchlistMovies);
        model.addAttribute("blacklist", blacklistMovies);
        model.addAttribute("watchlistTv", watchlistTvShows);
        model.addAttribute("blacklistTv", blacklistTvShows);
        model.addAttribute("review", reviewlist);
        model.addAttribute("reviewTv", reviewlistTv);
        model.addAttribute("watchlistSize", watchlistMovies.size());
        model.addAttribute("blacklistSize", blacklistMovies.size());
        model.addAttribute("watchlistTvSize", watchlistTvShows.size());
        model.addAttribute("blacklistTvSize", blacklistTvShows.size());
        model.addAttribute("reviewSize", reviewlist.size());
        model.addAttribute("reviewTvSize", reviewlistTv.size());
        model.addAttribute("message", message);
        model.addAttribute("queryOption", new QueryOption());
        if (token != null) {
            String loggedName = getCurrentUsername(token);
            User loggedIn = userRepository.findByUsername(loggedName);
            if (loggedIn.getPicture() != null) {
                loggedIn.setPicture64(Base64.getEncoder().encodeToString(loggedIn.getPicture()));
            }
            follow.setUsername(loggedName);
            follow.setFollow(username);
            Follow check = followRepository.findByUsernameAndFollow(loggedName, username);
            if (check == null) {
                follow.setFollowBool(false);
            }
            model.addAttribute("loggedin", loggedIn);
            Follow checkFollow = followRepository.findByUsernameAndFollow(loggedName, username);
            if (checkFollow != null) {
                follow.setFollowBool(true);
            }
        }
        model.addAttribute("follow", follow);
        return "profile";
    }

    @GetMapping("/account")
    public String account(@CookieValue(value="token", required=false) String token, Model model) {
        User user = new User();
        if (token != null) {
            String username = getCurrentUsername(token);
            user = userRepository.findByUsername(username);
            if (user.getPicture() != null) {
                user.setPicture64(Base64.getEncoder().encodeToString(user.getPicture()));
            }
            CriticApplications check = criticApplicationsRepository.findByUsername(username);
            if (check == null) {
                model.addAttribute("applied", false);
            } else {
                model.addAttribute("applied", true);
            }
        }
        model.addAttribute("user", user);
        model.addAttribute("queryOption", new QueryOption());
        return "account";
    }

    @PostMapping(value="/user/follow", consumes=MediaType.APPLICATION_JSON_VALUE, produces="application/json")
    @ResponseBody
    public StringResponse follow(@CookieValue(value="token", required=false) String token, @RequestBody Follow follow) {
        if (token != null) {
            String username = getCurrentUsername(token);
            if (follow.getUsername().equals(username)) {
                Follow check = followRepository.findByUsernameAndFollow(username, follow.getFollow());
                if (check == null) {
                    followRepository.save(follow);
                    return new StringResponse("OK");
                }
            }
        }
        return new StringResponse("ERROR");
    }

    @PostMapping(value="/user/unfollow", consumes=MediaType.APPLICATION_JSON_VALUE, produces="application/json")
    @ResponseBody
    public StringResponse unfollow(@CookieValue(value="token", required=false) String token, @RequestBody Follow follow) {
        if (token != null) {
            String username = getCurrentUsername(token);
            if (follow.getUsername().equals(username)) {
                Follow check = followRepository.findByUsernameAndFollow(username, follow.getFollow());
                if (check != null) {
                    followRepository.delete(check);
                    return new StringResponse("OK");
                }
            }
        }
        return new StringResponse("ERROR");
    }

    @GetMapping("/contact")
    public String getContact(@CookieValue(value="token", required=false) String token, Model model) {
        User user = new User();
        if (token != null) {
            String username = getCurrentUsername(token);
            user = userRepository.findByUsername(username);
            if (user.getPicture() != null) {
                user.setPicture64(Base64.getEncoder().encodeToString(user.getPicture()));
            }
            user.setPassword("");
            user.setResetToken("");
        }
        model.addAttribute("user", user);
        model.addAttribute("message", new Message());
        model.addAttribute("queryOption", new QueryOption());
        return "contact";
    }

    @PostMapping("/contact")
    public String postContact(HttpServletRequest req, Model model, @ModelAttribute User user) {
        Message m = new Message();
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setTo("jeffrey.fu@stonybrook.edu");
            helper.setText(user.getEmail() + " : " + user.getResetToken());
            helper.setSubject(user.getPassword());
            sender.send(message);
            m.setMessage("Email sent!");
        } catch (Exception ex) {
            m.setMessage("Email failed!");
        }
        user.setEmail("");
        user.setPassword("");
        user.setResetToken("");
        model.addAttribute("user", user);
        model.addAttribute("message", m);
        return "contact";
    }

    private void sendEmail(String email, String link) throws Exception {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo(email);
        helper.setText("Please use the following link to verify your account: \n" + link);
        helper.setSubject("Account Verification");
        sender.send(message);
    }

    private String getKey() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder key = new StringBuilder();
        Random num = new Random();
        while (key.length() < 20) {
            int index = (int) (num.nextFloat() * chars.length());
            key.append(chars.charAt(index));
        }
        return key.toString();
    }

    private String hash(String password) {
    String hashedPassword = null;
    try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] salt = { 82, 122, 43, 30, 47, 97, 4, 124, 31, 63, 108, 69, 83, 86, 125, 88, 98, 77, 111, 79, 71, 73, 100, 106, 8, 20, 95, 27, 38, 32, 61, 88 };
            md.update(salt);
            md.update(password.getBytes("UTF8"));
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < bytes.length ; i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            hashedPassword = sb.toString();
        } 
       catch (Exception e) {
            return null;
       }
    return hashedPassword;
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