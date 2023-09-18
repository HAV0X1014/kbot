package org.kbot.FunFeatures;

import org.javacord.api.entity.message.embed.EmbedBuilder;

public class Collatz {
    public static EmbedBuilder collatz(String number){
        EmbedBuilder e = new EmbedBuilder();
        String content;
        int input = Integer.parseInt(number);
        int inputOriginal = input;
        if (input > 100000000) {
            content = "Input must be under 100,000,000.";
        } else
        if (input < 1) {
            content = "Input must be bigger than 1.";
        } else {
            int steps = 0;
            int peak = input;
            long startTime = System.nanoTime();
            while (input != 1) {
                if ((input & 1) == 0) { // Using bitwise operator for faster optimization when checking if n is even.
                    input >>= 1; // Equivalent to dividing by 2 but using bitwise shift right operator for speed improvement.
                } else {
                    input = (3 * input) + 1;
                }
                steps++;
                if (input > peak) {
                    peak = input;
                }
            }
            long endTime = System.nanoTime();
            content = "Input: ``" + inputOriginal + "``\nPeak Number: ``" + peak + "``\nSteps :``" + steps + "``\nTime to Calculate :``" + (endTime - startTime) + " nanoseconds``";
        }

        e.setTitle("Collatz Conjecture");
        e.setDescription(content);
        return e;
    }
}
