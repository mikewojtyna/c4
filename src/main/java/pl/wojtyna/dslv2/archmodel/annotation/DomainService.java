package pl.wojtyna.dslv2.archmodel.annotation;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
public @interface DomainService {
    String boundedContext();
    String name();
}
