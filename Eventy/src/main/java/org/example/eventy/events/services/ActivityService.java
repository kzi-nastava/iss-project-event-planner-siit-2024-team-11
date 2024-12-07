package org.example.eventy.events.services;

import org.example.eventy.events.models.Activity;
import org.example.eventy.events.repositories.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityService {
    @Autowired
    private ActivityRepository activityRepository;

    public Activity save(Activity activity) {
        try {
            return activityRepository.save(activity);
        }
        catch (Exception e) {
            return null;
        }
    }
}
