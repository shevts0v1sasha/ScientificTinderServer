package ru.tinder.service.matchService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.tinder.constants.TimeConstants;
import ru.tinder.model.match.Match;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MatchService {

    private final List<Match> matches = Collections.synchronizedList(new ArrayList<>());

    public Optional<Match> addMatch(Match match, Consumer<Match> onMutualMatch) {
        long count = matches
                .stream()
                .filter(m -> m.getWhoId().equals(match.getWhoId()) && m.getWhomId().equals(match.getWhomId()))
                .count();
        if (count == 0) {
            matches.add(match);
            boolean isMutual = matches
                    .stream()
                    .anyMatch(m -> m.getWhoId().equals(match.getWhomId()) && m.getWhomId().equals(match.getWhoId()));
            if (isMutual) {
                onMutualMatch.accept(match);
            }
            return Optional.of(match);
        }
        return Optional.empty();
    }

    public List<Long> getMatchedUserIds(Long userId) {
        return matches
                .stream()
                .filter(match -> match.getWhoId().equals(userId))
                .map(Match::getWhomId)
                .collect(Collectors.toList());
    }

    @Scheduled(fixedDelay = TimeConstants.HALF_AN_HOUR_IN_MILLIS)
    private void updateMatches() {
        Iterator<Match> iterator = matches.iterator();
        while (iterator.hasNext()) {
            Match nextMatch = iterator.next();
            LocalDateTime matchedTime = nextMatch.getMatchedTime();
            LocalDateTime now = LocalDateTime.now();

            LocalDateTime minus30Minutes = now.minusMinutes(30);
            if (minus30Minutes.isAfter(matchedTime)) {
                iterator.remove();
            }
        }
    }


    public List<Long> getLikedIds(Long userId) {
        return matches
                .stream()
                .filter(match -> match.getWhoId().equals(userId))
                .map(Match::getWhomId)
                .collect(Collectors.toList());
    }
}
