package Model;

import algorithms.search.Solution;

import java.io.IOException;

public interface IModel {

    public void addObserver(Object vm);
    public void notifyAllObservers(Object arg);
    public void createGame(int rows, int cols);
    public void setPlayerLocation(String direction) throws FinishException;
    public Solution getSolution() throws NullPointerException;
    public byte[] getCompressedMaze();
    public void setGameDifficulty(Object difficulty);
    public void loadGame(byte[] data) throws IOException;
}
