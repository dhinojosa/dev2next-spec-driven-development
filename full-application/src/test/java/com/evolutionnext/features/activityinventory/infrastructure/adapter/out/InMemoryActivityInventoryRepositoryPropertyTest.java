package com.evolutionnext.features.activityinventory.infrastructure.adapter.out;

import com.evolutionnext.features.activityinventory.arbitrary.ActivityInventoryArbitrary;
import com.evolutionnext.features.activityinventory.domain.model.ActivityInventory;
import com.evolutionnext.features.activityinventory.domain.model.ActivityName;
import com.evolutionnext.features.activityinventory.domain.model.ActivityPriority;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryActivityInventoryRepositoryPropertyTest {
    @Property
    void everySavedActivityCanBeReadForItsUser(
        @ForAll("userNames") String userName,
        @ForAll("activityNames") String activityName,
        @ForAll("priorities") ActivityPriority priority) {
        var repository = new InMemoryActivityInventoryRepository();
        var inventory = new ActivityInventory()
            .add(new ActivityName(activityName), priority);

        repository.save(userName, inventory);

        assertThat(repository.findByUserName(userName).activities())
            .containsExactlyElementsOf(inventory.activities());
    }

    @Provide
    net.jqwik.api.Arbitrary<String> userNames() {
        return ActivityInventoryArbitrary.userNames();
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
