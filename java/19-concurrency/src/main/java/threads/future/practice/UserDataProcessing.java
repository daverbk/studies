package threads.future.practice;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

record UserProfile(String name, String email) {

}

record UserPreferences(boolean darkMode, String language) {

}

record UserSummary(String name, boolean darkMode) {

}


public class UserDataProcessing {

    static Random random = new Random();

    public static void main(String[] args) {
        var userProfileDataFuture = CompletableFuture.supplyAsync(() -> {
            sleepRandom(1000);
            return new UserProfile("Endgar", "Smith");
        });

        userProfileDataFuture.thenRun(() -> System.out.println("User profile data is ready"));

        var userPreferencesDataFuture = CompletableFuture.supplyAsync(() -> {
            sleepRandom(5000);
            return new UserPreferences(true, "en");
        });

        userPreferencesDataFuture.thenRun(() -> System.out.println("User preferences data is ready"));

        userProfileDataFuture
            .thenCombine(userPreferencesDataFuture, (profile, prefs) -> {
                sleepRandom(5000);
                return new UserSummary(profile.name(), prefs.darkMode());
            })
            .thenAccept(summary -> System.out.println("Final summary: " + summary))
            .join(); // wait for completion
    }

    private static void sleepRandom(int maxMillis) {
        try {
            Thread.sleep(random.nextInt(maxMillis));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
