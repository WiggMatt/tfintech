package ru.matthew.dao.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.matthew.dao.model.Event;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event e JOIN FETCH e.location")
    List<Event> findEvents(Specification<Event> specification);

    static Specification<Event> buildSpecification(String title, String locationSlug, LocalDate fromDate, LocalDate toDate) {
        List<Specification<Event>> specifications = new ArrayList<>();

        if (title != null) {
            var nameSpecification = new Specification<Event>() {
                @Override
                public Predicate toPredicate(Root<Event> event, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    return cb.equal(event.get("title"), title);
                }
            };
            specifications.add(nameSpecification);
        }

        if (locationSlug != null) {
            var locationSlugSpecification = new Specification<Event>() {
                @Override
                public Predicate toPredicate(Root<Event> event, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    return cb.equal(event.get("location").get("slug"), locationSlug);
                }
            };
            specifications.add(locationSlugSpecification);
        }

        if (fromDate != null) {
            var fromDateSpecification = new Specification<Event>() {
                @Override
                public Predicate toPredicate(Root<Event> event, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    return cb.greaterThan(event.get("date"), fromDate);
                }
            };
            specifications.add(fromDateSpecification);
        }

        if (toDate != null) {
            var toDateSpecification = new Specification<Event>() {
                @Override
                public Predicate toPredicate(Root<Event> event, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    return cb.lessThan(event.get("date"), toDate);
                }
            };
            specifications.add(toDateSpecification);
        }

        return specifications.stream().reduce(Specification::and).orElse(null);
    }
}
