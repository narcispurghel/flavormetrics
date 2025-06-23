package com.flavormetrics.api.factory;

import com.flavormetrics.api.entity.Tag;
import com.flavormetrics.api.model.projection.TagProjection;
import com.flavormetrics.api.model.request.AddRecipeRequest;
import com.flavormetrics.api.repository.TagRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class TagFactory {
    private final TagRepository tagRepository;

    TagFactory(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Transactional
    public Set<Tag> checkIfExistsOrElseSave(AddRecipeRequest req) {
        if (req == null) {
            throw new IllegalArgumentException("AddRecipeRequest cannot be null");
        }

        List<String> tagsName = Optional.ofNullable(req.tags())
                .orElse(Collections.emptySet())
                .stream()
                .map(t -> t.name().name())
                .toList();

        List<TagProjection> existing = tagRepository.getIdsAndNames(tagsName);
        List<String> existingNames = existing.stream().map(TagProjection::getName).toList();


        List<Tag> newTags = new ArrayList<>();
        if (!existing.isEmpty()) {
            newTags = Optional.ofNullable(req.tags())
                    .orElse(Collections.emptySet())
                    .stream()
                    .filter(t -> !existingNames.contains(t.name().name()))
                    .map(Tag::new)
                    .toList();
        } else {
            newTags = tagsName.stream()
                    .map(Tag::new)
                    .toList();
        }

        List<Tag> finalTags = existing
                .stream()
                .map(Tag::new)
                .collect(Collectors.toList());

        if (!newTags.isEmpty()) {
            List<Tag> saved = tagRepository.saveAll(newTags);
            finalTags.addAll(saved);
        }

        return Set.copyOf(finalTags);
    }
}
