package com.flavormetrics.api.factory;

import com.flavormetrics.api.entity.Allergy;
import com.flavormetrics.api.model.AllergyDto;
import com.flavormetrics.api.model.projection.AllergyProjection;
import com.flavormetrics.api.model.request.AddRecipeRequest;
import com.flavormetrics.api.repository.AllergyRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class AllergyFactory {
    private final AllergyRepository allergyRepository;

    AllergyFactory(AllergyRepository allergyRepository) {
        this.allergyRepository = allergyRepository;
    }

    @Transactional
    public Set<Allergy> checkIfExistsOrElseSave(Set<AllergyDto> allergies) {
        if (allergies == null) {
            throw new IllegalArgumentException("AddRecipeRequest cannot be null");
        }

        List<String> allergyNames = Optional.of(allergies)
                .orElse(Collections.emptySet())
                .stream()
                .map(a -> a.name().name())
                .toList();

        List<AllergyProjection> existing = allergyRepository.getIdsAndNames(allergyNames);
        List<String> existingNames = existing.stream().map(AllergyProjection::getName).toList();

        List<Allergy> newAllergies = new ArrayList<>();
        if (!existing.isEmpty()) {
            newAllergies = Optional.of(allergies)
                    .orElse(Collections.emptySet())
                    .stream()
                    .filter(a -> !existingNames.contains(a.name().name()))
                    .map(Allergy::new)
                    .toList();
        } else {
            newAllergies = allergyNames.stream()
                    .map(Allergy::new)
                    .toList();
        }

        List<Allergy> finalAllergies = existing
                .stream()
                .map(Allergy::new)
                .collect(Collectors.toList());

        if (!newAllergies.isEmpty()) {
            List<Allergy> saved = allergyRepository.saveAll(newAllergies);
            finalAllergies.addAll(saved);
        }

        return Set.copyOf(finalAllergies);
    }


}
