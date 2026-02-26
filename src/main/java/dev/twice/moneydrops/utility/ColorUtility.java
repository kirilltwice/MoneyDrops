package dev.twice.moneydrops.utility;

import java.util.List;
import java.util.Map;

import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NotNull;
import lombok.experimental.UtilityClass;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

@UtilityClass
public class ColorUtility {

    private static final MiniMessage MM = MiniMessage.miniMessage();

    public static Component colorize(final @NotNull String message) {
        return MM.deserialize(message).decoration(TextDecoration.ITALIC, false);
    }

    public static Component colorize(final @NotNull List<String> list) {
        return Component.join(
                JoinConfiguration.separator(Component.newline()),
                list.stream()
                        .map(MM::deserialize)
                        .toList()
        );
    }

    public static Component colorize(final @NotNull List<String> list,
                                     final Map<String, Object> placeholders) {
        final TagResolver[] resolvers = placeholders.entrySet().stream()
                .map(entry -> toPlaceholder(entry.getKey(), entry.getValue()))
                .toArray(TagResolver[]::new);

        return Component.join(
                JoinConfiguration.separator(Component.newline()),
                list.stream()
                        .map(line -> MM.deserialize(line, resolvers))
                        .toList()
        );
    }

    public static Component colorize(final @NotNull String message,
                                     final @NotNull Map<String, Object> placeholders) {
        final TagResolver[] resolvers = placeholders.entrySet().stream()
                .map(entry -> toPlaceholder(entry.getKey(), entry.getValue()))
                .toArray(TagResolver[]::new);

        return MM.deserialize(message, resolvers);
    }

    private static TagResolver toPlaceholder(final @Subst("") String key, final Object value) {
        return switch (value) {
            case final Component component -> Placeholder.component(key, component);
            case final String str -> Placeholder.parsed(key, str);
            default -> Placeholder.unparsed(key, String.valueOf(value));
        };
    }
}
