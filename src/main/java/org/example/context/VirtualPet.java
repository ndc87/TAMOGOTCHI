package org.example.context;

import org.example.state.HappyState;
import org.example.state.PetState;
import org.example.strategy.PetStrategy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class VirtualPet {
    private PetState state;
    private int fullness;
    private int happiness;
    private List<String> messageLog;

    public VirtualPet() {
        this.state = new HappyState();
        this.fullness = 100;
        this.happiness = 100;
        this.messageLog = new ArrayList<>();
    }

    public String apply(PetStrategy strategy) {
        messageLog.clear();
        addMessage("🎮 Action: " + strategy.getName());
        String result = state.handle(this, strategy);
        return result;
    }

    public void setState(PetState state) {
        this.state = state;
    }

    public void changeStats(int f, int h) {
        this.fullness = Math.max(0, Math.min(100, this.fullness + f));
        this.happiness = Math.max(0, Math.min(100, this.happiness + h));
    }

    public void addMessage(String message) {
        this.messageLog.add(message);
    }

    public int getFullness() { return fullness; }
    public int getHappiness() { return happiness; }
    public String getStateName() { return state.getName(); }
    public String getStateEmoji() { return state.getEmoji(); }
    public List<String> getMessageLog() { return new ArrayList<>(messageLog); }
    
    public void reset() {
        this.state = new HappyState();
        this.fullness = 100;
        this.happiness = 100;
        this.messageLog.clear();
        this.messageLog.add("🔄 Pet has been reset! Starting fresh.");
    }

    // Inner class for API response - avoiding separate package
    public static class PetStatusResponse {
        private String state;
        private String emoji;
        private int fullness;
        private int happiness;
        private List<String> messages;

        public PetStatusResponse() {}

        public PetStatusResponse(String state, String emoji, int fullness, int happiness, List<String> messages) {
            this.state = state;
            this.emoji = emoji;
            this.fullness = fullness;
            this.happiness = happiness;
            this.messages = messages;
        }

        public String getState() { return state; }
        public void setState(String state) { this.state = state; }
        public String getEmoji() { return emoji; }
        public void setEmoji(String emoji) { this.emoji = emoji; }
        public int getFullness() { return fullness; }
        public void setFullness(int fullness) { this.fullness = fullness; }
        public int getHappiness() { return happiness; }
        public void setHappiness(int happiness) { this.happiness = happiness; }
        public List<String> getMessages() { return messages; }
        public void setMessages(List<String> messages) { this.messages = messages; }
    }
}