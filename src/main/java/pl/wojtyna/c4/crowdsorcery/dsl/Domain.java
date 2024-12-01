package pl.wojtyna.c4.crowdsorcery.dsl;

import java.util.Set;

public interface Domain {

    Set<ArchElement> elements();

    Set<ArchElement> dependencies();

    String name();
}
