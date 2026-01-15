package Model;

import java.util.ArrayList;

/**
 * Stack implementation for Undo functionality
 * Stores deleted songs for restoration
 */
public class SongStack {
    private ArrayList<Song> stack;
    private static final int MAX_SIZE = 10; // Maximum undo operations

    public SongStack() {
        stack = new ArrayList<>();
    }

    // Push song onto stack
    public void push(Song song) {
        if (stack.size() >= MAX_SIZE) {
            stack.remove(0); // Remove oldest if full
        }
        stack.add(song);
    }

    // Pop song from stack
    public Song pop() {
        if (isEmpty()) {
            return null;
        }
        return stack.remove(stack.size() - 1);
    }

    // Peek at top song without removing
    public Song peek() {
        if (isEmpty()) {
            return null;
        }
        return stack.get(stack.size() - 1);
    }

    // Check if stack is empty
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    // Get current size
    public int size() {
        return stack.size();
    }

    // Clear the stack
    public void clear() {
        stack.clear();
    }
}