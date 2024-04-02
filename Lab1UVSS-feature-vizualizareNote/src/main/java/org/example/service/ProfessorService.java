package org.example.service;


import jakarta.persistence.EntityNotFoundException;
import org.example.model.Professors;
import org.example.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfessorService {

    @Autowired
    private ProfessorRepository professorRepository;

    public List<Professors> getAllProfessors() {
        return professorRepository.findAll();
    }

    public Optional<Professors> getProfessorById(Integer Id) {
        return professorRepository.findById(Id);
    }

    public Professors addProfessor(Professors professors) {
        professors.setProfessorID(null);
        return professorRepository.save(professors);
    }

    public boolean deleteProfessor(Integer Id) {
        Optional<Professors> existingProfessor = professorRepository.findById(Id);
        if (existingProfessor.isPresent()) {
            professorRepository.deleteById(Id);
            return true;
        } else throw new EntityNotFoundException("prof with id " + Id + " has not been found");
    }


}
