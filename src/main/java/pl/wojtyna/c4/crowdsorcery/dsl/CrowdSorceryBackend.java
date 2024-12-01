package pl.wojtyna.c4.crowdsorcery.dsl;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CrowdSorceryBackend extends ArchElement {

    public CrowdSorceryBackend(Database database, Email email, Payment payment) {
        uses(database);
        uses(email);
        uses(payment);
    }

    @Override
    public String name() {
        return "CrowdSorceryBackend";
    }

    @Override
    public VerificationResult verifyArchitecture() {
        var reflections = new Reflections("pl.wojtyna.c4.crowdsorcery.app", Scanners.TypesAnnotated);
        var crowdSorceryBackend = reflections.getTypesAnnotatedWith(pl.wojtyna.c4.crowdsorcery.app.backend.CrowdSorceryBackend.class);
        Set<String> violations = new HashSet<>();
        crowdSorceryBackend.forEach(
            crowdSorceryClass ->
                Arrays.stream(crowdSorceryClass.getConstructors()).findAny()
                      .ifPresent(constructor -> {
                          if (!Arrays.stream(constructor.getParameterTypes())
                                     .allMatch(type ->
                                                   type.isAnnotationPresent(pl.wojtyna.c4.crowdsorcery.app.backend.Database.class)
                                                   || type.isAnnotationPresent(
                                                       pl.wojtyna.c4.crowdsorcery.app.backend.Email.class)
                                                   || type.isAnnotationPresent(
                                                       pl.wojtyna.c4.crowdsorcery.app.backend.Payment.class))) {
                              violations.add(
                                  "CrowdSorceryBackend should have a constructor with Database, Email or Payment as parameters only");
                          }
                      }));
        return new VerificationResult(violations.isEmpty() ? VerificationStatus.PASSED : VerificationStatus.VIOLATED,
                                      violations);
    }
}
