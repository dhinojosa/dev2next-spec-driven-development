package com.evolutionnext.features.activityinventory.domain.model;

import com.evolutionnext.features.activityinventory.arbitrary.ActivityInventoryArbitrary;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;

import static org.assertj.core.api.Assertions.assertThat;

class ActivityInventoryPropertyTest {
    @Property
    void everyAddedActivityRemainsPresentWithItsPriority(
        @ForAll("activityNames") String activityName,
        @ForAll("priorities") ActivityPriority priority) {
        var inventory = new ActivityInventory()
            .add(new ActivityName(activityName), priority);

        assertThat(inventory.activities()).anySatisfy(activity -> {
            assertThat(activity.activityName().value()).isEqualTo(activityName);
            assertThat(activity.priority()).isEqualTo(priority);
        });
    }

    @Provide
    net.jqwik.api.Arbitrary<String> activityNames() {
        return ActivityInventoryArbitrary.activityNames();
    }

    @Provide
    net.jqwik.api.Arbitrary<ActivityPriority> priorities() {
        return ActivityInventoryArbitrary.priorities();
    }
}
