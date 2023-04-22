package com.github.sunnyst4r.lifeachievements.model;

import org.springframework.data.repository.CrudRepository;

public interface AchievementRepository extends CrudRepository<AchievementElement, Long> {
}