package com.wilsonfranca.saintseya;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by wilson on 07/04/18.
 */
class QuestPart implements Comparable <QuestPart> {

    private final Quest quest;

    private String id;

    private String next;

    private Reward reward;

    public QuestPart(Quest quest, String... properties){
        this.quest = quest;
        Arrays.asList(properties)
                .stream()
                .forEach(property -> {
                    if(property.contains("id")) {
                        this.id = property.substring(property.indexOf(":") + 1, property.length());
                    }

                    if(property.contains("next")) {
                        this.next = property.substring(property.indexOf(":") + 1, property.length());
                    }

                    if(property.contains("reward")) {
                        String rewardId = property.substring(property.indexOf(":") + 1, property.length());
                        this.reward = loadReward(rewardId);
                    }
                });
    }

    private Reward loadReward(String rewardId) {

        ClassLoader classLoader = getClass().getClassLoader();

        String path = classLoader.getResource(String.format("data/reward.data",
                rewardId.toLowerCase())).getPath();

        try (Stream<String> stringStream = Files.lines(Paths.get(path))) {

            return stringStream
                    .filter(s -> !"".equals(s) && s != null)
                    .map(line -> line.split(";"))
                    .map(Reward::new)
                    .filter(r -> r.getId().equals(rewardId))
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);


        } catch (IOException e) {
            throw new IllegalStateException("There is a problem loading the quest banner file");
        }
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestPart questPart = (QuestPart) o;
        return Objects.equals(id, questPart.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(QuestPart o) {
        return this.getId().compareTo(o.getId());
    }

    public String start(Player player) {
        String next;
        loadPart();
        if(this.next == null) {
            Scanner scanner = new Scanner(System.in);
            next = this.getId()+"_"+scanner.nextLine();
        } else {
            next = this.next;
        }
        if(hasReward()) {
            player.addXp(this.reward.getXp());
            player.addHp(this.reward.getHp());
        }
        return next;
    }

    private void loadPart() {

        ClassLoader classLoader = getClass().getClassLoader();

        String path = classLoader.getResource(String.format("quest/%s.txt",
                this.id.toLowerCase())).getPath();

        try (Stream<String> stringStream = Files.lines(Paths.get(path))) {

            String banner = stringStream
                    .filter(s -> !"".equals(s) && s != null)
                    .collect(Collectors.joining("\n"));
            System.out.println(banner);

        } catch (IOException e) {
            throw new IllegalStateException("There is a problem loading the quest banner file");
        }
    }

    private boolean hasReward() {
        return this.reward != null;
    }

}
