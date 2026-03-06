# MoneyDrops

![Minecraft 1.21.4](https://img.shields.io/badge/Minecraft-1.21.4-green)
![Java 21](https://img.shields.io/badge/Java-21-orange)
![Paper API](https://img.shields.io/badge/API-Paper-blue)

## Developer Guide (API)

To interact with the plugin API, use the global instance:

```java
MoneyDropsAPI.getInstance();
```

---

## 1. Event (Bukkit Event)

### MoneyDropEvent

This event is triggered **before the reward is given** (before the Vault transaction).  
The event implements the `Cancellable` interface.

Methods:

- `getKiller()` — returns the player who killed the entity.
- `getVictim()` — returns the killed `Entity`.
- `getBaseReward()` — returns the base reward calculated from the config.
- `getFinalReward()` — returns the final reward.
- `setFinalReward(double)` — allows overriding the final reward.
- `setCancelled(true)` — cancels the reward completely.

If the event is cancelled, the reward calculation will stop.

---

## 2. Custom Reward Modifiers

Allows other plugins to dynamically modify rewards.  
Example use cases: **VIP bonuses, night bonuses, faction bonuses, etc.**

Example:

```java
MoneyDropsAPI.getInstance().registerModifier((player, victim, currentReward) -> {
    if (player.hasPermission("vip.bonus")) {
        return currentReward * 1.5;
    }
    return currentReward;
});
```

---

## 3. Temporary Player Boosters

You can give players temporary reward multipliers.

Internally the plugin uses `ConcurrentHashMap`.  
The multiplier works until:
- the time expires
- the server restarts

Example:

```java
long duration = TimeUnit.MINUTES.toMillis(30);

// Give player a x2.0 multiplier for 30 minutes
MoneyDropsAPI.getInstance().addTemporaryMultiplier(player, 2.0, duration);
```

---

## 4. Farm Protection (Spawn Reason Filtering)

Prevents money drops from artificially spawned mobs.

Uses Paper API `getEntitySpawnReason`.

Example:

```java
// Disable money drops from mobs spawned by spawners
MoneyDropsAPI.getInstance().addIgnoredSpawnReason(CreatureSpawnEvent.SpawnReason.SPAWNER);

// Disable money drops from spawn eggs
MoneyDropsAPI.getInstance().addIgnoredSpawnReason(CreatureSpawnEvent.SpawnReason.SPAWNER_EGG);
```

---

If you have suggestions or improvements, feel free to contribute.
