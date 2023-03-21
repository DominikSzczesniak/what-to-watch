package pl.szczesniak.dominik.videos.model;

import lombok.AllArgsConstructor;
import pl.szczesniak.dominik.videos.domain.VideoType;

@AllArgsConstructor
public class TvShow implements VideoType {
    private final String title;

    @Override
    public void addToList(String title) {

    }

    @Override
    public void removeFromList(String title) {

    }
}
