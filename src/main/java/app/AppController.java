package app;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.servlet.http.Cookie;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;

import static app.JWT.EXPIRATION_TIME;
import static app.JWT.SECRET;

@Controller
public class AppController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TvShowRepository tvRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private CastRepository castRepository;

    @Autowired
    private TvCastRepository tvCastRepository;

    @Autowired
    private TvCreatedRepository tvCreatedRepository;

    @Autowired
    private MoviesDirectedRepository moviesDirectedRepository;

    @Autowired
    private MoviesWrittenRepository moviesWrittenRepository;

    @Autowired
    private CriticApplicationsRepository criticApplicationsRepository;

    @Autowired
    private OpeningWeekRepository openingWeekRepository;

    @Autowired
    private OpeningSoonRepository openingSoonRepository;

    @Autowired
    private MovieBoxOfficeRepository movieBoxOfficeRepository;

    @GetMapping("/")
    public String getHome(@CookieValue(value="token", required=false) String token, Model model) {
        User user = new User();
        if (token != null) {
            String username = getCurrentUsername(token);
            user = userRepository.findByUsername(username);
            if (user.getPicture() != null) {
                user.setPicture64(Base64.getEncoder().encodeToString(user.getPicture()));
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
        model.addAttribute("queryOption", new QueryOption());
        model.addAttribute("user", user);
        return "index";
    }

    @GetMapping("/about")
    public String getAbout(@CookieValue(value="token", required=false) String token, Model model) {
        User user = new User();
        if (token != null) {
            String username = getCurrentUsername(token);
            user = userRepository.findByUsername(username);
            if (user.getPicture() != null) {
                user.setPicture64(Base64.getEncoder().encodeToString(user.getPicture()));
            }
        }
        model.addAttribute("queryOption", new QueryOption());
        model.addAttribute("user", user);
        return "about";
    }

    @GetMapping("/terms")
    public String getTerms(@CookieValue(value="token", required=false) String token, Model model) {
        User user = new User();
        if (token != null) {
            String username = getCurrentUsername(token);
            user = userRepository.findByUsername(username);
            if (user.getPicture() != null) {
                user.setPicture64(Base64.getEncoder().encodeToString(user.getPicture()));
            }
        }
        model.addAttribute("queryOption", new QueryOption());
        model.addAttribute("user", user);
        return "terms";
    }

    @GetMapping("/help")
    public String getHelp(@CookieValue(value="token", required=false) String token, Model model) {
        User user = new User();
        if (token != null) {
            String username = getCurrentUsername(token);
            user = userRepository.findByUsername(username);
            if (user.getPicture() != null) {
                user.setPicture64(Base64.getEncoder().encodeToString(user.getPicture()));
            }
        }
        model.addAttribute("queryOption", new QueryOption());
        model.addAttribute("user", user);
        return "help";
    }

    @GetMapping("/gallery")
    public String getGallery(@CookieValue(value="token", required=false) String token, Model model) {
        User user = new User();
        if (token != null) {
            String username = getCurrentUsername(token);
            user = userRepository.findByUsername(username);
            if (user.getPicture() != null) {
                user.setPicture64(Base64.getEncoder().encodeToString(user.getPicture()));
            }
        }
        model.addAttribute("queryOption", new QueryOption());
        model.addAttribute("user", user);
        return "gallery";
    }

    @PostMapping(value="/applycritic", consumes=MediaType.APPLICATION_JSON_VALUE, produces="application/json")
    @ResponseBody
    public StringResponse applyCritic(
                                @CookieValue(value="token", required=false) String token, 
                                @RequestBody User user) {
        if (token != null) {
            String username = getCurrentUsername(token);
            User check = userRepository.findByUsername(username);
            if (check.getPassword().equals(hash(user.getPassword()))) {
                CriticApplications appliedBefore = criticApplicationsRepository.findByUsername(username);
                if (appliedBefore == null) {
                    CriticApplications c = new CriticApplications();
                    c.setUsername(username);
                    c.setText(user.getResetToken());
                    criticApplicationsRepository.save(c);
                    return new StringResponse("OK");
                }
            }
        }
        return new StringResponse("ERROR");
    }

    @GetMapping("/actor")
    public String getActor(
        @RequestParam("id") Integer id,
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
        Person person = personRepository.findById(id);
        model.addAttribute("person", person);
        model.addAttribute("queryOption", new QueryOption());
        model.addAttribute("user", user);
        List<Cast> cast = castRepository.findAllByPersonId(id);
        List<MoviesWritten> written = moviesWrittenRepository.findAllByPersonId(id);
        List<MoviesDirected> directed = moviesDirectedRepository.findAllByPersonId(id);
        List<TvCast> tvCast = tvCastRepository.findAllByPersonId(id);
        List<TvCreated> tvCreated = tvCreatedRepository.findAllByPersonId(id);
        List<Movie> castIn = new ArrayList<Movie>();
        List<Movie> writtenIn = new ArrayList<Movie>();
        List<Movie> directedIn = new ArrayList<Movie>();
        List<TvShow> tvCastIn = new ArrayList<TvShow>();
        List<TvShow> createdIn = new ArrayList<TvShow>();
        for (Cast c : cast) {
            Movie m = movieRepository.findById(c.getMovieId());
            castIn.add(m);
        }
        for (MoviesWritten w : written) {
            Movie m = movieRepository.findById(w.getMovieId());
            writtenIn.add(m);
        }
        for (MoviesDirected d : directed) {
            Movie m = movieRepository.findById(d.getMovieId());
            directedIn.add(m);
        }
        for (TvCast c : tvCast) {
            TvShow m = tvRepository.findById(c.getTvId());
            tvCastIn.add(m);
        }
        for (TvCreated t : tvCreated) {
            TvShow m = tvRepository.findById(t.getTvId());
            createdIn.add(m);
        }
        model.addAttribute("cast", castIn);
        model.addAttribute("castSize", castIn.size());
        model.addAttribute("written", writtenIn);
        model.addAttribute("writtenSize", writtenIn.size());
        model.addAttribute("directed", directedIn);
        model.addAttribute("directedSize", directedIn.size());
        model.addAttribute("tvcast", tvCastIn);
        model.addAttribute("tvcastSize", tvCastIn.size());
        model.addAttribute("created", createdIn);
        model.addAttribute("createdSize", createdIn.size());
        model.addAttribute("role", cast);
        model.addAttribute("tvrole", tvCast);
        return "actor";
    }

    @PostMapping("/allmovies")
    public String allMovies(Model model, @CookieValue(value="token", required=false) String token, 
                            @ModelAttribute QueryOption queryOption) {
        User user = new User();
        if (token != null) {
            String username = getCurrentUsername(token);
            user = userRepository.findByUsername(username);
            if (user.getPicture() != null) {
                user.setPicture64(Base64.getEncoder().encodeToString(user.getPicture()));
            }
        }
        String[] terms = queryOption.getQuery().trim().split("\\s+");
        List<List<Movie>> movies = new ArrayList<List<Movie>>();
        for (String term : terms) {
            List<Movie> movieList = movieRepository.findByTitleIgnoreCaseContaining(term);
            movies.add(movieList);
        }
        List<Movie> firstList = movies.get(0);
        for (int i = 1; i < movies.size(); i++) {
            List<Movie> check = movies.get(i);
            List<Movie> newFirstList = new ArrayList<Movie>();
            for (Movie m : check) {
                if (firstList.contains(m)) {
                    newFirstList.add(m);
                }
            }
            firstList.clear();
            for (Movie m : newFirstList) {
                firstList.add(m);
            }
        }
        for (Movie edge : firstList) {
            if (edge.getAvgRating() == null) {
                edge.setAvgRating(0);
            }
            if (edge.getReleaseDate() == null) {
                Movie copy = movieRepository.findById(1712);
                edge.setReleaseDate(copy.getReleaseDate());
            }
        }
        List<Movie> nameListM = new ArrayList<>(firstList);
        List<Movie> rateListM = new ArrayList<>(firstList);
        List<Movie> dateListM = new ArrayList<>(firstList);
        List<Movie> boxListM = new ArrayList<>(firstList);
        if (firstList.size() != 0) {
            Collections.sort(nameListM, new Comparator<Movie>() {
                public int compare(Movie one, Movie two) {
                    return one.getTitle().compareTo(two.getTitle());
                }
            });
            Collections.sort(rateListM, new Comparator<Movie>() {
                public int compare(Movie one, Movie two) {
                    return two.getAvgRating().compareTo(one.getAvgRating());
                }
            });
            Collections.sort(dateListM, new Comparator<Movie>() {
                public int compare(Movie one, Movie two) {
                    return two.getReleaseDate().compareTo(one.getReleaseDate());
                }
            });
            Collections.sort(boxListM, new Comparator<Movie>() {
                public int compare(Movie one, Movie two) {
                    return two.getBoxOffice().compareTo(one.getBoxOffice());
                }
            });
        }
        model.addAttribute("displayMname", nameListM);
        model.addAttribute("displayMrate", rateListM);
        model.addAttribute("displayMdate", dateListM);
        model.addAttribute("displayMbox", boxListM);
        model.addAttribute("displayM", firstList);
        model.addAttribute("displayMSize", firstList.size());
        model.addAttribute("queryOptions", queryOption);
        model.addAttribute("user", user);
        return "movieresults";
    }

    @PostMapping("/alltv")
    public String allTv(Model model, @CookieValue(value="token", required=false) String token, 
                            @ModelAttribute QueryOption queryOption) {
        User user = new User();
        if (token != null) {
            String username = getCurrentUsername(token);
            user = userRepository.findByUsername(username);
            if (user.getPicture() != null) {
                user.setPicture64(Base64.getEncoder().encodeToString(user.getPicture()));
            }
        }
        String[] terms = queryOption.getQuery().trim().split("\\s+");
        List<List<TvShow>> tvs = new ArrayList<List<TvShow>>();
        for (String term : terms) {
            List<TvShow> tvList = tvRepository.findByTitleIgnoreCaseContaining(term);
            tvs.add(tvList);
        }
        List<TvShow> firstList = tvs.get(0);
        for (int i = 1; i < tvs.size(); i++) {
            List<TvShow> check = tvs.get(i);
            List<TvShow> newFirstList = new ArrayList<TvShow>();
            for (TvShow t : check) {
                if (firstList.contains(t)) {
                    newFirstList.add(t);
                }
            }
            firstList.clear();
            for (TvShow t : newFirstList) {
                firstList.add(t);
            }
        }
        List<TvShow> nameListT = new ArrayList<>(firstList);
        List<TvShow> rateListT = new ArrayList<>(firstList);
        List<TvShow> dateListT = new ArrayList<>(firstList);
        if (firstList.size() != 0) {
            Collections.sort(nameListT, new Comparator<TvShow>() {
                public int compare(TvShow one, TvShow two) {
                    return one.getTitle().compareTo(two.getTitle());
                }
            });
            Collections.sort(rateListT, new Comparator<TvShow>() {
                public int compare(TvShow one, TvShow two) {
                    return two.getAvgRating().compareTo(one.getAvgRating());
                }
            });
            Collections.sort(dateListT, new Comparator<TvShow>() {
                public int compare(TvShow one, TvShow two) {
                    return two.getReleaseDate().compareTo(one.getReleaseDate());
                }
            });
        }
        model.addAttribute("displayTname", nameListT);
        model.addAttribute("displayTrate", rateListT);
        model.addAttribute("displayTdate", dateListT);
        model.addAttribute("displayT", firstList);
        model.addAttribute("displayTSize", firstList.size());
        model.addAttribute("queryOptions", queryOption);
        model.addAttribute("user", user);
        return "tvresults";
    }

    @PostMapping("/allcelebrities")
    public String allCelebrities(Model model, @CookieValue(value="token", required=false) String token, 
                            @ModelAttribute QueryOption queryOption) {
        User user = new User();
        if (token != null) {
            String username = getCurrentUsername(token);
            user = userRepository.findByUsername(username);
            if (user.getPicture() != null) {
                user.setPicture64(Base64.getEncoder().encodeToString(user.getPicture()));
            }
        }
        String[] terms = queryOption.getQuery().trim().split("\\s+");
        List<List<Person>> persons = new ArrayList<List<Person>>();
        for (String term : terms) {
            List<Person> personList = personRepository.findByPersonNameIgnoreCaseContaining(term);
            persons.add(personList);
        }
        List<Person> firstList = persons.get(0);
        for (int i = 1; i < persons.size(); i++) {
            List<Person> check = persons.get(i);
            List<Person> newFirstList = new ArrayList<Person>();
            for (Person p : check) {
                if (firstList.contains(p)) {
                    newFirstList.add(p);
                }
            }
            firstList.clear();
            for (Person p : newFirstList) {
                firstList.add(p);
            }
        }
        List<Person> nameListP = new ArrayList<>(firstList);
        if (firstList.size() != 0) {
            Collections.sort(nameListP, new Comparator<Person>() {
                public int compare(Person one, Person two) {
                    return one.getPersonName().compareTo(two.getPersonName());
                }
            });
        }
        model.addAttribute("displayPname", nameListP);
        model.addAttribute("displayP", firstList);
        model.addAttribute("displayPSize", firstList.size());
        model.addAttribute("queryOptions", queryOption);
        model.addAttribute("user", user);
        return "celebrityresults";
    }

    @PostMapping("/search")
    public String search(Model model, @CookieValue(value="token", required=false) String token, @ModelAttribute QueryOption queryOption) {
        User user = new User();
        if (token != null) {
            String username = getCurrentUsername(token);
            user = userRepository.findByUsername(username);
            if (user.getPicture() != null) {
                user.setPicture64(Base64.getEncoder().encodeToString(user.getPicture()));
            }
        }
        String[] terms = queryOption.getQuery().trim().split("\\s+");
        if (queryOption.getDropdown().equals("Movies")) {
            List<List<Movie>> movies = new ArrayList<List<Movie>>();
            for (String term : terms) {
                List<Movie> movieList = movieRepository.findByTitleIgnoreCaseContaining(term);
                movies.add(movieList);
            }
            List<Movie> firstList = movies.get(0);
            for (int i = 1; i < movies.size(); i++) {
                List<Movie> check = movies.get(i);
                List<Movie> newFirstList = new ArrayList<Movie>();
                for (Movie m : check) {
                    if (firstList.contains(m)) {
                        newFirstList.add(m);
                    }
                }
                firstList.clear();
                for (Movie m : newFirstList) {
                    firstList.add(m);
                }
            }
            for (Movie edge : firstList) {
            if (edge.getAvgRating() == null) {
                edge.setAvgRating(0);
            }
            if (edge.getReleaseDate() == null) {
                Movie copy = movieRepository.findById(1712);
                edge.setReleaseDate(copy.getReleaseDate());
            }
        }
            List<Movie> nameListM = new ArrayList<>(firstList);
            List<Movie> rateListM = new ArrayList<>(firstList);
            List<Movie> dateListM = new ArrayList<>(firstList);
            List<Movie> boxListM = new ArrayList<>(firstList);
            if (firstList.size() != 0) {
                Collections.sort(nameListM, new Comparator<Movie>() {
                    public int compare(Movie one, Movie two) {
                        return one.getTitle().compareTo(two.getTitle());
                    }
                });
                Collections.sort(rateListM, new Comparator<Movie>() {
                    public int compare(Movie one, Movie two) {
                        return two.getAvgRating().compareTo(one.getAvgRating());
                    }
                });
                Collections.sort(dateListM, new Comparator<Movie>() {
                    public int compare(Movie one, Movie two) {
                        return two.getReleaseDate().compareTo(one.getReleaseDate());
                    }
                });
                Collections.sort(boxListM, new Comparator<Movie>() {
                    public int compare(Movie one, Movie two) {
                        return two.getBoxOffice().compareTo(one.getBoxOffice());
                    }
                });
            }
            model.addAttribute("displayMname", nameListM);
            model.addAttribute("displayMrate", rateListM);
            model.addAttribute("displayMdate", dateListM);
            model.addAttribute("displayMbox", boxListM);
            model.addAttribute("displayM", firstList);
            model.addAttribute("displayMSize", firstList.size());
        } else if (queryOption.getDropdown().equals("TV Shows")) {
            List<List<TvShow>> tvs = new ArrayList<List<TvShow>>();
            for (String term : terms) {
                List<TvShow> tvList = tvRepository.findByTitleIgnoreCaseContaining(term);
                tvs.add(tvList);
            }
            List<TvShow> firstList = tvs.get(0);
            for (int i = 1; i < tvs.size(); i++) {
                List<TvShow> check = tvs.get(i);
                List<TvShow> newFirstList = new ArrayList<TvShow>();
                for (TvShow t : check) {
                    if (firstList.contains(t)) {
                        newFirstList.add(t);
                    }
                }
                firstList.clear();
                for (TvShow t : newFirstList) {
                    firstList.add(t);
                }
            }
            List<TvShow> nameListT = new ArrayList<>(firstList);
            List<TvShow> rateListT = new ArrayList<>(firstList);
            List<TvShow> dateListT = new ArrayList<>(firstList);
            if (firstList.size() != 0) {
                Collections.sort(nameListT, new Comparator<TvShow>() {
                    public int compare(TvShow one, TvShow two) {
                        return one.getTitle().compareTo(two.getTitle());
                    }
                });
                Collections.sort(rateListT, new Comparator<TvShow>() {
                    public int compare(TvShow one, TvShow two) {
                        return two.getAvgRating().compareTo(one.getAvgRating());
                    }
                });
                Collections.sort(dateListT, new Comparator<TvShow>() {
                    public int compare(TvShow one, TvShow two) {
                        return two.getReleaseDate().compareTo(one.getReleaseDate());
                    }
                });
            }
            model.addAttribute("displayTname", nameListT);
            model.addAttribute("displayTrate", rateListT);
            model.addAttribute("displayTdate", dateListT);
            model.addAttribute("displayT", firstList);
            model.addAttribute("displayTSize", firstList.size());
        } else if (queryOption.getDropdown().equals("Celebrities")) {
            List<List<Person>> persons = new ArrayList<List<Person>>();
            for (String term : terms) {
                List<Person> personList = personRepository.findByPersonNameIgnoreCaseContaining(term);
                persons.add(personList);
            }
            List<Person> firstList = persons.get(0);
            for (int i = 1; i < persons.size(); i++) {
                List<Person> check = persons.get(i);
                List<Person> newFirstList = new ArrayList<Person>();
                for (Person p : check) {
                    if (firstList.contains(p)) {
                        newFirstList.add(p);
                    }
                }
                firstList.clear();
                for (Person p : newFirstList) {
                    firstList.add(p);
                }
            }
            List<Person> nameListP = new ArrayList<>(firstList);
            if (firstList.size() != 0) {
                Collections.sort(nameListP, new Comparator<Person>() {
                    public int compare(Person one, Person two) {
                        return one.getPersonName().compareTo(two.getPersonName());
                    }
                });
            }
            model.addAttribute("displayPname", nameListP);
            model.addAttribute("displayP", firstList);
            model.addAttribute("displayPSize", firstList.size());
        } else {
            List<List<Movie>> movies = new ArrayList<List<Movie>>();
            List<List<TvShow>> tvs = new ArrayList<List<TvShow>>();
            List<List<Person>> persons = new ArrayList<List<Person>>();
            for (String term : terms) {
                List<Movie> movieList = movieRepository.findByTitleIgnoreCaseContaining(term);
                movies.add(movieList);
                List<TvShow> tvList = tvRepository.findByTitleIgnoreCaseContaining(term);
                tvs.add(tvList);
                List<Person> personList = personRepository.findByPersonNameIgnoreCaseContaining(term);
                persons.add(personList);
            }
            List<Movie> firstListM = movies.get(0);
            for (int i = 1; i < movies.size(); i++) {
                List<Movie> check = movies.get(i);
                List<Movie> newFirstList = new ArrayList<Movie>();
                for (Movie m : check) {
                    if (firstListM.contains(m)) {
                        newFirstList.add(m);
                    }
                }
                firstListM.clear();
                for (Movie m : newFirstList) {
                    firstListM.add(m);
                }
            }
            List<TvShow> firstListT = tvs.get(0);
            for (int i = 1; i < tvs.size(); i++) {
                List<TvShow> check = tvs.get(i);
                List<TvShow> newFirstList = new ArrayList<TvShow>();
                for (TvShow t : check) {
                    if (firstListT.contains(t)) {
                        newFirstList.add(t);
                    }
                }
                firstListT.clear();
                for (TvShow t : newFirstList) {
                    firstListT.add(t);
                }
            }
            List<Person> firstListP = persons.get(0);
            for (int i = 1; i < persons.size(); i++) {
                List<Person> check = persons.get(i);
                List<Person> newFirstList = new ArrayList<Person>();
                for (Person p : check) {
                    if (firstListP.contains(p)) {
                        newFirstList.add(p);
                    }
                }
                firstListP.clear();
                for (Person p : newFirstList) {
                    firstListP.add(p);
                }
            }
            for (Movie edge : firstListM) {
                if (edge.getAvgRating() == null) {
                    edge.setAvgRating(0);
                }
                if (edge.getReleaseDate() == null) {
                    Movie copy = movieRepository.findById(1712);
                    edge.setReleaseDate(copy.getReleaseDate());
                }
            }
            List<Movie> nameListM = new ArrayList<>(firstListM);
            List<Movie> rateListM = new ArrayList<>(firstListM);
            List<Movie> dateListM = new ArrayList<>(firstListM);
            List<Movie> boxListM = new ArrayList<>(firstListM);
            List<TvShow> nameListT = new ArrayList<>(firstListT);
            List<TvShow> rateListT = new ArrayList<>(firstListT);
            List<TvShow> dateListT = new ArrayList<>(firstListT);
            List<Person> nameListP = new ArrayList<>(firstListP);
            if (firstListM.size() != 0) {
                Collections.sort(nameListM, new Comparator<Movie>() {
                    public int compare(Movie one, Movie two) {
                        return one.getTitle().compareTo(two.getTitle());
                    }
                });
                Collections.sort(rateListM, new Comparator<Movie>() {
                    public int compare(Movie one, Movie two) {
                        return two.getAvgRating().compareTo(one.getAvgRating());
                    }
                });
                Collections.sort(dateListM, new Comparator<Movie>() {
                    public int compare(Movie one, Movie two) {
                        return two.getReleaseDate().compareTo(one.getReleaseDate());
                    }
                });
                Collections.sort(boxListM, new Comparator<Movie>() {
                    public int compare(Movie one, Movie two) {
                        return two.getBoxOffice().compareTo(one.getBoxOffice());
                    }
                });
            }
            if (firstListT.size() != 0) {
                Collections.sort(nameListT, new Comparator<TvShow>() {
                    public int compare(TvShow one, TvShow two) {
                        return one.getTitle().compareTo(two.getTitle());
                    }
                });
                Collections.sort(rateListT, new Comparator<TvShow>() {
                    public int compare(TvShow one, TvShow two) {
                        return two.getAvgRating().compareTo(one.getAvgRating());
                    }
                });
                Collections.sort(dateListT, new Comparator<TvShow>() {
                    public int compare(TvShow one, TvShow two) {
                        return two.getReleaseDate().compareTo(one.getReleaseDate());
                    }
                });
            }
            if (firstListP.size() != 0) {
                Collections.sort(nameListP, new Comparator<Person>() {
                    public int compare(Person one, Person two) {
                        return one.getPersonName().compareTo(two.getPersonName());
                    }
                });
            }
            model.addAttribute("displayMname", nameListM);
            model.addAttribute("displayMrate", rateListM);
            model.addAttribute("displayMdate", dateListM);
            model.addAttribute("displayMbox", boxListM);
            model.addAttribute("displayTname", nameListT);
            model.addAttribute("displayTrate", rateListT);
            model.addAttribute("displayTdate", dateListT);
            model.addAttribute("displayPname", nameListP);
            model.addAttribute("displayM", firstListM);
            model.addAttribute("displayT", firstListT);
            model.addAttribute("displayP", firstListP);
            model.addAttribute("displayMSize", firstListM.size());
            model.addAttribute("displayTSize", firstListT.size());
            model.addAttribute("displayPSize", firstListP.size());
        }
        model.addAttribute("queryOptions", queryOption);
        model.addAttribute("user", user);
        return "results";
    }

    private String getCurrentUsername(String token) {
        String username = Jwts.parser()
                    .setSigningKey(SECRET.getBytes())
                    .parseClaimsJws(token.replace("Bearer", ""))
                    .getBody()
                    .getSubject();
        return username;
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
}