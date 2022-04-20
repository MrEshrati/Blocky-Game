package com.poulstar.blocky.core;

public interface GameEngineListener {
    public void gameTick(GameEngine gameEngine);
    public void moneyUpdate(GameEngine gameEngine);
    public void gameEnded(GameEngine gameEngine);
}
