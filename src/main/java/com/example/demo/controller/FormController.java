package com.example.demo.controller;

import com.example.demo.model.Field;
import com.example.demo.model.Form;
import com.example.demo.service.FormService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/forms")
public class FormController {

    @Autowired
    private FormService formService;

    @GetMapping("/")
    public List<Form> getForms() {
        return formService.getAllForms();
    }

    @PostMapping("/")
    public Form createForm(@RequestBody Form form) {
        return formService.saveForm(form);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Form> getForm(@PathVariable Long id) {
        Form form = formService.getFormById(id);
        return ResponseEntity.ok(form);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Form> updateForm(@PathVariable Long id, @RequestBody Form form) {
        formService.updateForm(id, form);
        return ResponseEntity.ok(form);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteForm(@PathVariable Long id) {
        formService.deleteForm(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/publish")
    public ResponseEntity<Void> publishForm(@PathVariable Long id) {
        try {
            formService.publishForm(id);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/published")
    public List<Form> getPublishedForms() {
        return formService.getPublishedForms();
    }

    @GetMapping("/{id}/fields")
    public ResponseEntity<List<Field>> getFieldsByFormId(@PathVariable Long id) {
        try {
            List<Field> fields = formService.getFieldsByFormId(id);
            return ResponseEntity.ok(fields);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}/fields")
    public ResponseEntity<Form> updateFieldsByFormId(@PathVariable Long id, @RequestBody List<Field> fields) {
        try {
            Form updatedForm = formService.updateFieldsByFormId(id, fields);
            return ResponseEntity.ok(updatedForm);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
