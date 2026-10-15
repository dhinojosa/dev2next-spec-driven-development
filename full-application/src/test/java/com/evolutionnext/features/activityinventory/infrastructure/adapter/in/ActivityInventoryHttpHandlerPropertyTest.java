package com.evolutionnext.features.activityinventory.infrastructure.adapter.in;

import com.evolutionnext.AccountApplication;
import com.evolutionnext.features.account.infrastructure.adapter.out.InMemoryAccountRepository;
import com.evolutionnext.features.activityinventory.arbitrary.ActivityInventoryArbitrary;
import com.evolutionnext.features.activityinventory.domain.model.ActivityPriority;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ActivityInventoryHttpHandlerPropertyTest {
    @Property(tries = 20)
    void everySuccessfulPostIsVisibleThroughGet(
        @ForAll("activityNames") String activityName,
        @ForAll("priorities") ActivityPriority priority)
        throws Exception {
        var server = new AccountApplication().start(0, new InMemoryAccountRepository());
        try {
            var baseUri = URI.create("http://localhost:" + server.getAddress().getPort());
            var httpClient = HttpClient.newHttpClient();
            var userName = "property-" + UUID.randomUUID();
            var registration = post(httpClient, baseUri, "/account/register",
                "userName=%s&password=correct-horse-battery-staple".formatted(encode(userName)),
                null);
            var cookie = registration.headers().firstValue("Set-Cookie")
                .orElseThrow()
                .split(";", 2)[0];

            var added = post(httpClient, baseUri, "/activity-inventory/activity",
                "activityName=%s&priority=%s".formatted(encode(activityName), priority.name()),
                cookie);
            var read = get(httpClient, baseUri, "/activity-inventory", cookie);

            assertThat(added.statusCode()).isEqualTo(200);
            assertThat(read.body()).contains(activityName);
            assertThat(read.body()).contains(titleCase(priority) + " priority");
        } finally {
            server.stop(0);
        }
    }

    private static HttpResponse<String> get(HttpClient httpClient,
                                            URI baseUri,
                                            String path,
                                            String cookie) throws Exception {
        var request = HttpRequest.newBuilder(baseUri.resolve(path))
            .header("Cookie", cookie)
            .GET()
            .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static HttpResponse<String> post(HttpClient httpClient,
                                             URI baseUri,
                                             String path,
                                             String body,
                                             String cookie) throws Exception {
        var builder = HttpRequest.newBuilder(baseUri.resolve(path))
            .header("Content-Type", "application/x-www-form-urlencoded");
        if (cookie != null) {
            builder.header("Cookie", cookie);
        }
        return httpClient.send(builder.POST(HttpRequest.BodyPublishers.ofString(body)).build(),
            HttpResponse.BodyHandlers.ofString());
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private static String titleCase(ActivityPriority priority) {
        var value = priority.name().toLowerCase();
        return Character.toUpperCase(value.charAt(0)) + value.substring(1);
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
