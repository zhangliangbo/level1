package xxl.exercise;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * 打印顺序
 */
public class PrintOrder {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] weights = line.split(" +");
            Deque<Task> deque = new LinkedList<>();
            for (int i = 0; i < weights.length; i++) {
                deque.offerLast(new Task(i, Integer.parseInt(weights[i])));
            }
            while (!deque.isEmpty()) {
                Task current = deque.pollFirst();
                boolean moved = false;
                for (Task t : deque) {
                    if (current.weight < t.weight) {
                        deque.offerLast(current);
                        moved = true;
                        break;
                    }
                }
                if (!moved) {
                    System.out.println(current.index + " ");
                }
            }
        }
    }

    static class Task {
        int index;
        int weight;

        public Task(int index, int weight) {
            this.index = index;
            this.weight = weight;
        }
    }
}
