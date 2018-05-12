package com.arthurl.wolfbot.game.engine.requests;

class ExpectedAnswer {
    long request_time;
    IRequest request;

    ExpectedAnswer(long request_time, IRequest request) {
        this.request_time = request_time;
        this.request = request;
    }
}
