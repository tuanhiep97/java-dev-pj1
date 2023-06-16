package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/note")
public class NoteController {
    private final UserService userService;
    private final NoteMapper noteMapper;

    public NoteController(UserService userService, NoteMapper noteMapper) {
        this.userService = userService;
        this.noteMapper = noteMapper;
    }

    @PostMapping
    public String insertNote(@ModelAttribute Note note, Principal principal, Model model){
        String error = null;
        String username = principal.getName();
        Integer userId = userService.getUser(username).getUserId();

        if(note.getNoteId() != null){
            int result = noteMapper.updateNote(note);
            if(result < 0){
                error = "There was an error upload your note. Please try again.";
            }
        }
        else{
            int result = noteMapper.insertNote(note, userId);
            if(result < 0){
                error = "There was an error edit your note. Please try again.";
            }
        }
        if(error != null){
            model.addAttribute("error", error);
        }
        else{
            model.addAttribute("success", true);
        }
        return "result";
    }

    @GetMapping("/delete")
    public String deleteNote(@RequestParam Integer noteId, Model model){
        String error = null;

        int result = noteMapper.deleteNote(noteId);
        if(result < 0){
            error = "There was an error delete your note. Please try again.";
        }
        if(error != null){
            model.addAttribute("error", error);
        }
        else{
            model.addAttribute("success", true);
        }
        return "result";
    }

}
