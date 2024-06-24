/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highscore;

/**
 * The HighScore class represents a record holding a player's name and score.
 * This record encapsulates the essential data of a high score entry.
 */
public record HighScore(String name, int score) {

    /**
     * Provides a string representation of the HighScore record.
     * @return A string representing the name and score of the high score entry.
     */
    @Override
    public String toString() {
        return name + "    " + score;
    }
}
