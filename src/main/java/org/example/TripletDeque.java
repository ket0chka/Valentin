package org.example;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class TripletDeque<T> implements Deque<T>, Containerable {
    public TripletDeque(int tripletLenght) {
        TripletLenght = tripletLenght;
    }

    private int TripletLenght = 5;
    private int MaxQcontainersize = 1000;
    private int containersize;
    private int size;
    private int maxQsize;

    public TripletDeque() {
        this(5, 1000); // Используем значения по умолчанию
    }

    public TripletDeque(int containersize, int maxQsize) {
        this.containersize = containersize;
        this.maxQsize = maxQsize;
        this.first = new Container<>(containersize);
        this.last = first; // Добавляем точку с запятой
    }

    @Override
    public Object[] getContainerByIndex(int cIndex) {
        Container<T> current = first;
        int index = 0;
        while (current != null) {
            if (index == cIndex) {
                return current.elem;
            }
            current = current.next;
            index++;
        }
        return new Object[0]; // Возвращаем пустой массив, если индекс не найден
    }

    private static class Container<T> {
        T[] elem;
        Container<T> next;
        Container<T> previous;
        int firstindex;
        int lastindex;

        Container(int containersize) {
            this.elem = (T[]) new Object[containersize];
            this.lastindex = -1;
            this.next = null;
        }
    }

    private Container<T> first;
    private Container<T> last;

    @Override
    public void addFirst(T t) {
        if (size >= maxQsize) {
            throw new IllegalStateException("Заполнен");
        }
        if (t == null) {
            throw new NullPointerException("Добавление пустого элемента");
        }
        if (first.firstindex <= 0) {
            Container<T> newContainer = new Container<>(containersize);
            newContainer.next = first;
            first.previous = newContainer;
            first = newContainer;
            first.lastindex = containersize - 1;
            first.firstindex = containersize - 1;
        }
        first.elem[first.firstindex--] = t;
        size++;
    }

    @Override
    public void addLast(T t) {
        if (size >= maxQsize) {
            throw new IllegalStateException("Очередь заполнена");
        }
        if (t == null) {
            throw new NullPointerException("Добавление пустого элемента");
        }

        // Если last равен null, означает, что это первый элемент
        if (last == null) {
            last = new Container<>(containersize);
            last.firstindex = 0;
            last.lastindex = -1; // Инициализация для первого элемента
            first = last; // Установка first на последний для указания на первый элемент
        }

        // Проверяем, заполнен ли текущий контейнер
        if (last.lastindex == containersize - 1) {
            Container<T> newContainer = new Container<>(containersize);
            newContainer.previous = last;
            last.next = newContainer;
            last = newContainer;
            last.firstindex = 0;
            last.lastindex = -1;
        }

        last.elem[++last.lastindex] = t; // Добавляем элемент
        size++; // Увеличиваем размер
    }

    @Override
    public boolean offerFirst(T t) {
        if (size >= maxQsize) {
            return false;
        }
        if (t == null) {
            throw new NullPointerException("Добавление пустого элемента");
        }
        if (first.firstindex == 0) {
            Container<T> newContainer = new Container<>(containersize);
            newContainer.next = first;
            first.previous = newContainer;
            first = newContainer;
            first.lastindex = containersize - 1;
            first.firstindex = containersize - 1;
        }
        first.elem[first.firstindex--] = t;
        size++;
        return true;
    }

    @Override
    public boolean offerLast(T t) {
        if (size >= maxQsize) {
            return false;
        }
        if (t == null) {
            throw new NullPointerException("Добавление пустого элемента");
        }
        if (last.lastindex == containersize - 1) {
            Container<T> newContainer = new Container<>(containersize);
            newContainer.previous = last;
            last.next = newContainer;
            last = newContainer;
            last.firstindex = 0;
            last.lastindex = -1;
        }
        last.elem[++last.lastindex] = t;
        size++;
        return true;
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException("Очередь пуста");
        }
        if (first == null || first.firstindex > first.lastindex) {
            throw new NoSuchElementException("В первом контейнере нет элементов");
        }
        T element = first.elem[first.firstindex + 1];
        for (int i = first.firstindex + 1; i < first.lastindex; i++) {
            first.elem[i] = first.elem[i + 1];
        }
        first.elem[first.lastindex] = null;
        first.lastindex--;
        size--;

        if (first.firstindex > first.lastindex) {
            if (first.next != null) {
                first = first.next;
                first.previous = null;
            } else {
                first = null;
                last = null;
            }
        }

        return element;
    }


    @Override
    public T removeLast() {
        if (size == 0) {
            throw new NoSuchElementException("Очередь пуста");
        }

        int lastIndex = containersize - 1;
        while (lastIndex >= 0 && last.elem[lastIndex] == null) {
            lastIndex--;
        }

        if (lastIndex >= 0) {
            T element = last.elem[lastIndex];
            last.elem[lastIndex] = null;
            size--;

            if (lastIndex == 0 && last.previous != null) {
                last = last.previous;
                last.next = null;
            }

            return element;
        }

        while (last.previous != null) {
            last = last.previous;
            last.next = null;
            lastIndex = containersize - 1;
            while (lastIndex >= 0 && last.elem[lastIndex] == null) {
                lastIndex--;
            }
            if (lastIndex >= 0) {
                T element = last.elem[lastIndex];
                last.elem[lastIndex] = null;
                size--;
                return element;
            }
        }
//
        throw new NoSuchElementException("Очередь пуста");
    }

    @Override
    public T pollFirst() {
        if (size == 0) {
            return null;
        }
        T elem = first.elem[first.firstindex + 1];
        first.elem[first.firstindex + 1] = null;
        if (first.firstindex == containersize - 2) {
            if (first.next != null) {
                first = first.next;
                first.previous = null;
            } else {
                first = null;
                last = null;
            }
        } else {
            first.firstindex++;
        }
        size--;
        return elem;
    }

    @Override
    public T pollLast() {
        if (size == 0) {
            return null;
        }
        T elem = last.elem[last.lastindex];
        last.elem[last.lastindex] = null;
        if (last.lastindex == 0) {
            if (last.previous != null) {
                last = last.previous;
                last.next = null;
            } else {
                first = null;
                last = null;
            }
        } else {
            last.lastindex--;
        }
        size--;
        return elem;
    }

    @Override
    public T getFirst() {
        if (size == 0) {
            throw new NoSuchElementException("Очередь пуста");
        }
        return first.elem[first.firstindex + 1];
    }

    @Override
    public T getLast() {
        if (size == 0) {
            throw new NoSuchElementException("Очередь пуста");
        }
        // Проверяем, есть ли элементы в последнем контейнере
        if (last.lastindex < 0) {
            // Если последний контейнер пуст, переходим к предыдущему
            last = last.previous;
        }
        return last.elem[last.lastindex];
    }
    @Override
    public T peekFirst() {
        if (size == 0) {
            return null;
        }
        return first.elem[first.firstindex + 1];
    }

    @Override
    public T peekLast() {
        if (size == 0) {
            return null;
        }
        return last.elem[last.lastindex];
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        if (o == null) {
            throw new NullPointerException("Передан пустой элемент");
        }
        if (size == 0) {
            return false;
        }
        boolean isFound = false;
        Container<T> currentContainer = first;
        while (currentContainer != null) {
            for (int i = currentContainer.firstindex + 1; i <= currentContainer.lastindex; i++) {
                if (currentContainer.elem[i] != null && currentContainer.elem[i].equals(o)) {
                    for (int j = i; j < currentContainer.lastindex; j++) {
                        currentContainer.elem[j] = currentContainer.elem[j + 1];
                    }
                    currentContainer.elem[currentContainer.lastindex] = null;
                    currentContainer.lastindex--;
                    size--;
                    isFound = true;
                    break;
                }
            }
            if (isFound) {
                break;
            }
            currentContainer = currentContainer.next;
        }
        return isFound;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        if (o == null) {
            throw new NullPointerException("Передан пустой элемент");
        }
        if (size == 0) {
            return false;
        }
        boolean isFound = false;
        Container<T> currentContainer = last;
        while (currentContainer != null) {
            for (int i = currentContainer.lastindex; i > currentContainer.firstindex; i--) {
                if (currentContainer.elem[i] != null && currentContainer.elem[i].equals(o)) {
                    for (int j = i; j < currentContainer.lastindex; j++) {
                        currentContainer.elem[j] = currentContainer.elem[j + 1];
                    }
                    currentContainer.elem[currentContainer.lastindex] = null;
                    currentContainer.lastindex--;
                    size--;
                    isFound = true;
                    break;
                }
            }
            if (isFound) {
                break;
            }
            currentContainer = currentContainer.previous;
        }
        return isFound;
    }

    @Override
    public boolean add(T t) {
        if (t == null) {
            throw new NullPointerException("Попытка добавить пустой элемент");
        }
        if (size >= maxQsize) {
            throw new IllegalStateException("Очередь заполнена");
        }
        if (last == null) {
            last = new Container<>(containersize);
            first = last;
        } else {
            if (last.elem[last.lastindex] != null && !last.elem[last.lastindex].getClass().equals(t.getClass())) {
                throw new ClassCastException("Тип элементов не совпадает");
            }
            if (last.lastindex == containersize - 1) {
                Container<T> newContainer = new Container<>(containersize);
                newContainer.previous = last;
                last.next = newContainer;
                last = newContainer;
                last.firstindex = 0;
                last.lastindex = -1;
            }
        }
        last.elem[++last.lastindex] = t;
        size++;
        return true;
    }

    @Override
    public boolean offer(T t) {
        if (size >= maxQsize) {
            return false;
        }
        if (t == null) {
            throw new NullPointerException("Добавление пустого элемента");
        }
        if (last == null) {
            last = new Container<>(containersize);
            first = last;
        } else {
            if (last.elem[last.lastindex] != null && !last.elem[last.lastindex].getClass().equals(t.getClass())) {
                throw new ClassCastException("Тип элементов не совпадает");
            }
            if (last.lastindex == containersize - 1) {
                Container<T> newContainer = new Container<>(containersize);
                newContainer.previous = last;
                last.next = newContainer;
                last = newContainer;
                last.firstindex = 0;
                last.lastindex = -1;
            }
        }
        last.elem[++last.lastindex] = t;
        size++;
        return true;
    }

    @Override
    public T remove() {
        if (size == 0) {
            throw new NoSuchElementException("Очередь пуста");
        }

        // Проверка на наличие элементов в первом контейнере
        if (first == null || first.firstindex > first.lastindex) {
            return (T) "0";
        }

        T elem = first.elem[first.firstindex]; // Получаем элемент по текущему индексу
        first.elem[first.firstindex] = null;   // Удаляем ссылку на элемент
        first.firstindex++;                     // Увеличиваем индекс первого элемента
        size--;                                  // Уменьшаем общий размер очереди

        // Если первый контейнер стал пустым, переходим к следующему контейнеру
        if (first.firstindex > first.lastindex) {
            if (first.next != null) {
                first = first.next;              // Переходим к следующему контейнеру
                first.previous = null;           // Обнуляем ссылку на предыдущий контейнер
            } else {
                // Если нет следующих контейнеров, очищаем ссылки
                first = null;
                last = null;
            }
        }

        return elem;
    }

    @Override
    public T poll() {
        if (size == 0) {
            return null;
        }
        T elem = first.elem[first.firstindex + 1];
        first.elem[first.firstindex + 1] = null;
        if (first.firstindex == containersize - 2) {
            if (first.next != null) {
                first = first.next;
                first.previous = null;
            } else {
                first = null;
                last = null;
            }
        } else {
            first.firstindex++;
        }
        size--;
        return elem;
    }

    @Override
    public T element() {
        if (size == 0) {
            throw new NoSuchElementException("Очередь пуста");
        }
        return first.elem[first.firstindex + 1];
    }

    @Override
    public T peek() {
        if (size == 0) {
            return null;
        }
        return first.elem[first.firstindex + 1];
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        if (c == null) {
            throw new NullPointerException("Передана пустая коллекция");
        }
        boolean flg = false;
        for (T elem : c) {
            if (elem == null) {
                throw new NullPointerException("Добавление пустого элемента");
            }
            if (size >= maxQsize) {
                throw new IllegalStateException("Очередь заполнена");
            }
            if (last == null) {
                last = new Container<>(containersize);
                first = last;
            } else {
                if (last.elem[last.lastindex] != null && !last.elem[last.lastindex].getClass().equals(elem.getClass())) {
                    throw new ClassCastException("Элементы разных типов");
                }
                if (last.lastindex == containersize - 1) {
                    Container<T> newContainer = new Container<>(containersize);
                    newContainer.previous = last;
                    last.next = newContainer;
                    last = newContainer;
                    last.firstindex = 0;
                    last.lastindex = -1;
                }
            }
            last.elem[++last.lastindex] = elem;
            size++;
            flg = true;
        }
        return flg;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c) {
            while (remove(o)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Container<T> currentContainer = first;
        while (currentContainer != null) {
            for (int i = currentContainer.firstindex + 1; i <= currentContainer.lastindex; i++) {
                if (currentContainer.elem[i] != null && !c.contains(currentContainer.elem[i])) {
                    currentContainer.elem[i] = null;
                    size--;
                    modified = true;
                }
            }
            currentContainer = currentContainer.next;
        }
        return modified;
    }

    @Override
    public void clear() {
        first = new Container<>(containersize);
        last = first;
        size = 0;
    }

    @Override
    public void push(T t) {
        addFirst(t);
    }

    @Override
    public T pop() {
        return removeFirst();
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            return false;
        }

        Container<T> current = first;
        while (current != null) {
            for (int i = current.firstindex + 1; i <= current.lastindex; i++) {
                if (current.elem[i] != null && current.elem[i].equals(o)) {
                    // Сдвигаем элементы справа от удаляемого элемента влево
                    for (int j = i; j < current.lastindex; j++) {
                        current.elem[j] = current.elem[j + 1];
                    }
                    current.elem[current.lastindex] = null;
                    current.lastindex--;
                    size--;

                    // Если последний контейнер стал пустым, переходим к предыдущему контейнеру
                    if (current.lastindex < current.firstindex + 1) {
                        if (current.previous != null) {
                            current = current.previous;
                            current.next = null;
                        } else {
                            first = null;
                            last = null;
                        }
                    }

                    return true;
                }
            }
            current = current.next;
        }

        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object elem : c) {
            if (!contains(elem)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean contains(Object o) {
        Container<T> currentContainer = first;
        while (currentContainer != null) {
            for (int i = currentContainer.firstindex + 1; i <= currentContainer.lastindex; i++) {
                if (currentContainer.elem[i] != null && currentContainer.elem[i].equals(o)) {
                    return true;
                }
            }
            currentContainer = currentContainer.next;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new TripletDequeIterator();
    }

    private class TripletDequeIterator implements Iterator<T> {
        private Container<T> currentContainer = first;
        private int currentIndex = first.firstindex + 1;

        @Override
        public boolean hasNext() {
            return currentContainer != null && (currentIndex <= currentContainer.lastindex || currentContainer.next != null);
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T elem = currentContainer.elem[currentIndex++];
            if (currentIndex > currentContainer.lastindex) {
                currentContainer = currentContainer.next;
                currentIndex = currentContainer != null ? currentContainer.firstindex + 1 : 0;
            }
            return elem;
        }
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int index = 0;
        for (T elem : this) {
            result[index++] = elem;
        }
        return result;
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        if (a.length < size) {
            a = (T1[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        int index = 0;
        for (T elem : this) {
            a[index++] = (T1) elem;
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public Iterator<T> descendingIterator() {
        return new DescendingTripletDequeIterator();
    }

    private class DescendingTripletDequeIterator implements Iterator<T> {
        private Container<T> currentContainer = last;
        private int currentIndex = last.lastindex;

        @Override
        public boolean hasNext() {
            return currentContainer != null && (currentIndex >= currentContainer.firstindex + 1 || currentContainer.previous != null);
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T elem = currentContainer.elem[currentIndex--];
            if (currentIndex < currentContainer.firstindex + 1) {
                currentContainer = currentContainer.previous;
                currentIndex = currentContainer != null ? currentContainer.lastindex : 0;
            }
            return elem;
        }
    }

}