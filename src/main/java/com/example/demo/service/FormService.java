package com.example.demo.service;

import com.example.demo.model.Field;
import com.example.demo.model.Form;
import com.example.demo.repository.FormRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class FormService {
    @Autowired
    private FormRepository formRepository;

    public List<Form> getAllForms() {
        return formRepository.findAll();
    }

    public Form saveForm(Form form) {
        return formRepository.save(form);
    }

    public Form getFormById(Long id) {
        return formRepository.findById(id).orElse(null);
    }

    public void updateForm(Long id, Form updatedForm) {
        Form existingForm = formRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Form not found"));

        existingForm.setName(updatedForm.getName());
        existingForm.setPublished(updatedForm.isPublished());

        List<Field> existingFields = existingForm.getFields();
        List<Field> updatedFields = updatedForm.getFields();

        for (Iterator<Field> iterator = existingFields.iterator(); iterator.hasNext();) {
            Field existingField = iterator.next();
            Optional<Field> correspondingField = updatedFields.stream().filter(f -> f.getId() != null && f.getId().equals(existingField.getId())).findFirst();

            if (correspondingField.isPresent()) {
                Field updatedField = correspondingField.get();
                existingField.setName(updatedField.getName());
                existingField.setLabel(updatedField.getLabel());
                existingField.setType(updatedField.getType());
                existingField.setDefaultValue(updatedField.getDefaultValue());
            } else {
                iterator.remove();
            }
        }

        for (Field updatedField : updatedFields) {
            if (updatedField.getId() == null) {
                updatedField.setForm(existingForm);
                existingFields.add(updatedField);
            }
        }

        formRepository.save(existingForm);
    }

    public void deleteForm(Long id) {
        formRepository.deleteById(id);
    }

    public void publishForm(Long id) {
        Form form = formRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Form with id " + id + " not found"));
        form.setPublished(true);
        formRepository.save(form);
    }


    public List<Form> getPublishedForms() {
        return formRepository.findAll().stream()
                .filter(Form::isPublished)
                .toList();
    }

    public List<Field> getFieldsByFormId(Long formId) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new EntityNotFoundException("Form with id " + formId + " not found"));
        return form.getFields();
    }
    @Transactional
    public Form updateFieldsByFormId(Long formId, List<Field> fields) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new EntityNotFoundException("Form with id " + formId + " not found"));

        form.getFields().clear();
        fields.forEach(field -> {
            field.setForm(form);
            form.getFields().add(field);
        });

        return formRepository.save(form);
    }

}