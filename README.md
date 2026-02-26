<div align="center">
  <h1>MoneyDrops</h1>
  <p>
    <img src="https://img.shields.io/badge/Minecraft-1.21.4-brightgreen?style=for-the-badge&logo=minecraft" alt="Minecraft 1.21.4" />
    <img src="https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk" alt="Java 21" />
    <img src="https://img.shields.io/badge/PaperMC-API-blue?style=for-the-badge" alt="Paper API" />
  </p>
</div>

## 🛠 Инструкция для разработчиков (API)
Для работы с API плагина используйте глобальный инстанс `MoneyDropsAPI.getInstance()`.

### 1. Событие (Bukkit Event)
**`MoneyDropEvent`** вызывается прямо перед начислением денег (до обращения к Vault). Работает с интерфейсом `Cancellable`.
* `getKiller()` — Получить Игрока-убийцу.
* `getVictim()` — Узнать убитую сущность `Entity`.
* `getBaseReward()` — Узнать изначальную награда, рассчитанную конфигом.
* `getFinalReward()` / `setFinalReward(double)` — Переопределить итоговую награду к выдаче.
* `setCancelled(true)` - Запретить выдачу (При отмене ивент прекращает расчет).

### 2. Свои модификаторы рассчета (RewardModifiers)
Позволит вашему стороннему плагину динамически пересчитывать награды (например: х2 ночью, дополнительные деньги для фракции или клана убийцы):
```java
// На этот лямбда-метод плагин будет опираться при каждом расчете
MoneyDropsAPI.getInstance().registerModifier((player, victim, currentReward) -> {
    if (player.hasPermission("vip.bonus")) {
        return currentReward * 1.5;
    }
    return currentReward;
});
```

### 3. Временные бустеры для игроков
Метод позволяет выдать временный множитель монет (работает через `ConcurrentHashMap`, сохраняется до перезапуска сервера или окончания срока):
```java
long duration = TimeUnit.MINUTES.toMillis(30);
// Выдать игроку множитель x2.0 на 30 минут 
MoneyDropsAPI.getInstance().addTemporaryMultiplier(player, 2.0, duration);
```

### 4. Игнорирование ферм 
Блокировка выпадения денег для искусственно созданных или заспавненных плагином сторонних существ (поддерживается Paper API `getEntitySpawnReason`)
```java
// Отключение дропа монет с мобов, рожденных из Рассадников монстров (Спавнеров)
MoneyDropsAPI.getInstance().addIgnoredSpawnReason(CreatureSpawnEvent.SpawnReason.SPAWNER);

// Аналогично для Яиц призыва Спавна:
MoneyDropsAPI.getInstance().addIgnoredSpawnReason(CreatureSpawnEvent.SpawnReason.SPAWNER_EGG);
```
