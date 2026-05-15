package pl.wojtyna.velocity.archmodel;

public class VelocityAppVerificationBadExample {

    public static void main(String[] args) {
        var contextMap = new CityBikeRentalContextMap();
        var architecture = new CityBikeRentalArchitecture(contextMap).architecture();
        var result = architecture.verifyApplication("pl.wojtyna.velocity.app.bad");
        System.out.println("Verification: " + result.status());
        if (result.violations().isEmpty()) {
            System.out.println("Violations: []");
        } else {
            result.violations().forEach(v -> System.out.println("- " + v));
        }
    }
}
