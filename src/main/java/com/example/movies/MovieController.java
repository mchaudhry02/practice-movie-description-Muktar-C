package com.example.movies;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MovieController {

    private final MovieRepository movieRepository;
    private final GeminiService geminiService;

    public MovieController(MovieRepository movieRepository, GeminiService geminiService) {
        this.movieRepository = movieRepository;
        this.geminiService = geminiService;
    }

    // Show homepage with all movies
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("movies", movieRepository.findAll());
        model.addAttribute("newMovie", new Movie());
        return "index";
    }

    // Handle form submission
    @PostMapping("/add")
    public String addMovie(@ModelAttribute Movie movie) {
        // Call Gemini to generate description
        String description = geminiService.generateDescription(movie.getTitle());
        movie.setDescription(description);
        movieRepository.save(movie);
        return "redirect:/";
    }
}