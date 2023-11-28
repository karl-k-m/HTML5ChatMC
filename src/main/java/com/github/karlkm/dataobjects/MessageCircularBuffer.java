package com.github.karlkm.dataobjects;

public class MessageCircularBuffer {
    private MessageObject[] buffer;
    private int head;
    private int tail;
    private int count;
    private int capacity;

    public MessageCircularBuffer(int capacity) {
        this.capacity = capacity;
        this.buffer = new MessageObject[capacity];
        this.head = 0;
        this.tail = 0;
        this.count = 0;
    }

    public void add(MessageObject message) {
        buffer[tail] = message;
        tail = (tail + 1) % capacity;

        if (count < capacity) {
            count++;
        } else {
            head = (head + 1) % capacity; // Overwrite the oldest message
        }
    }

    public MessageObject get(int index) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
        return buffer[(head + index) % capacity];
    }

    public int size() {
        return count;
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public MessageObject[] getMessages() {
        MessageObject[] messages = new MessageObject[count];
        for (int i = 0; i < count; i++) {
            messages[i] = get(i);
        }
        return messages;
    }

    public boolean contains(MessageObject msg) {
        for (int i = 0; i < count; i++) {
            if (msg.toString().equals(buffer[(head + i) % capacity].toString())) {
                return true;
            }
        }
        return false;
    }
}
