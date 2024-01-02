package me.julionxn.cinematiccreeper.util.mixins;

import me.julionxn.cinematiccreeper.managers.paths.Path;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface PathAwareData {

    void cinematiccreeper$setPath(Path path);

    @Nullable
    Path cinematiccreeper$getPath();

    void cinematiccreeper$addPath(Path path);

    List<Path> cinematiccreeper$getPaths();

    void cinematiccreeper$removePath(Path path);

}
