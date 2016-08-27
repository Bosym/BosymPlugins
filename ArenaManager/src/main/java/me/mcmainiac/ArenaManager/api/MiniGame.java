package me.mcmainiac.ArenaManager.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
public @interface MiniGame {
    ArenaType type();
    String name();
}
