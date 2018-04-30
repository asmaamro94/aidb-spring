package com.example.todos.controller;

import com.example.todos.exception.ResourceNotFoundException;
import com.example.todos.model.Todo;
import com.example.todos.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/todos")
public class TodoController
{
    @Autowired
    TodoRepository todoRepository;


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String todos(Model model){
        List<Todo> todos = todoRepository.findAllByOrderByCreatedAtDesc();
        if(!model.containsAttribute("newTodo")) {
            model.addAttribute("newTodo", new Todo());
        }
        model.addAttribute("todos", todos);
        return "todos";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createTodo(@Valid Todo newTodo, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.newTodo", bindingResult);
            redirectAttributes.addFlashAttribute("newTodo", newTodo);
            redirectAttributes.addFlashAttribute("message", "Something went wrong.");
            return "redirect:/todos/";
        }
        todoRepository.save(newTodo);
        redirectAttributes.addFlashAttribute("message", "new todo item has been created.");
        return "redirect:/todos/";
    }


    @GetMapping("/{todoId}/edit")
    public String editTodo(@PathVariable(value = "todoId") Long todoId, Model model){
        Todo editedTodo = todoRepository.findById(todoId)
                .orElseThrow(() -> new ResourceNotFoundException("Todo", "todoId", todoId));
        if(!model.containsAttribute("editedTodo")) {
            model.addAttribute("editedTodo", editedTodo);
        }
        return "edit";
    }


    @RequestMapping(value = "/{todoId}/update", method = RequestMethod.POST)
    public String updateTodo(@PathVariable(value = "todoId") Long todoId,
                             @Valid Todo editedTodo, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        Todo updatedTodo = todoRepository.findById(todoId)
            .orElseThrow(() -> new ResourceNotFoundException("Todo", "todoId", todoId));
        editedTodo.setId(todoId);
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.editedTodo", bindingResult);
            redirectAttributes.addFlashAttribute("editedTodo", editedTodo);
            redirectAttributes.addFlashAttribute("message", "Something went wrong.");
            return String.format("redirect:/todos/%s/edit", todoId);
        }
        updatedTodo.setContent(editedTodo.getContent());
        updatedTodo.setCompleted(editedTodo.isCompleted());
        todoRepository.save(updatedTodo);
        redirectAttributes.addFlashAttribute("message", "Todo item has been updated.");
        return String.format("redirect:/todos/%s/edit", todoId);
    }

    @RequestMapping(value = "/{todoId}/delete", method = RequestMethod.POST)
    public String deleteTodo(@PathVariable(value = "todoId") Long todoId, RedirectAttributes redirectAttributes){
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new ResourceNotFoundException("Todo", "todoId", todoId));

        todoRepository.delete(todo);
        redirectAttributes.addFlashAttribute("message", "Todo item has been deleted.");
        return String.format("redirect:/todos/", todoId);
    }
}




