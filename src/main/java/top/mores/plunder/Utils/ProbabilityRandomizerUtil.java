package top.mores.plunder.Utils;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class ProbabilityRandomizerUtil {
    private final NavigableMap<Double, String> probabilityMap = new TreeMap<>();
    private final Random random = new Random();
    private double totalWeight = 0;

    public ProbabilityRandomizerUtil() {
        // 初始化每个等级的概率
        addEntry("普通", 40);
        addEntry("高级", 25);
        addEntry("稀有", 15);
        addEntry("史诗", 10);
        addEntry("传奇", 5);
        addEntry("绝世", 3);
        addEntry("神话", 2);
    }

    // 添加等级和对应权重
    private void addEntry(String rarity, double weight) {
        if (weight > 0) {
            totalWeight += weight;
            probabilityMap.put(totalWeight, rarity);
        }
    }

    // 随机选择等级
    public String getRandomRarity() {
        double randomValue = random.nextDouble() * totalWeight;
        return probabilityMap.higherEntry(randomValue).getValue();
    }
}