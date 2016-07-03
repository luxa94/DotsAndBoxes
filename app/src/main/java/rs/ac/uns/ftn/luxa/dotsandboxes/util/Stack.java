package rs.ac.uns.ftn.luxa.dotsandboxes.util;

import org.androidannotations.annotations.EBean;

import java.util.Iterator;
import java.util.NoSuchElementException;

@EBean
public class Stack<Item> implements Iterable<Item> {
    private Node<Item> first;
    private int N;

    private static class Node<Item> {
        private Item item;
        private Node<Item> next;
    }

    public Stack() {
        first = null;
        N = 0;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public int size() {
        return N;
    }

    public void push(Item item) {
        final Node<Item> oldFirst = first;
        first = new Node<>();
        first.item = item;
        first.next = oldFirst;
        N++;
    }

    public Item pop() {
        if (isEmpty()) {
            throw new NoSuchElementException("Stack underflow");
        }
        final Item item = first.item;
        first = first.next;
        N--;
        return item;
    }

    public Item peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Stack underflow");
        }
        return first.item;
    }

    public void clear() {
        first = null;
        N = 0;
    }

    public String toString() {
        final StringBuilder s = new StringBuilder();
        for (Item item : this)
            s.append(item.toString());
        s.append(" ");
        return s.toString();
    }

    public Iterator<Item> iterator() {
        return new ListIterator<>(first);
    }

    private class ListIterator<T> implements Iterator<T> {
        private Node<T> current;

        public ListIterator(Node<T> first) {
            current = first;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            final T item = current.item;
            current = current.next;
            return item;
        }

        @Override
        public void remove() {

        }
    }

}

