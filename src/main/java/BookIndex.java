import java.io.*;
import java.util.*;

public class BookIndex {
    private LinkedList<IndexEntry> index;

    public BookIndex() {
        index = new LinkedList<>();
    }

    private static class IndexEntry {
        String term;
        TreeSet<Integer> pages;

        IndexEntry(String term) {
            this.term = term;
            this.pages = new TreeSet<>();
        }

        void addPage(int page) {
            pages.add(page);
        }

        void removePage(int page) {
            pages.remove(page);
        }

        boolean isEmpty() {
            return pages.isEmpty();
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(term + " ");
            Iterator<Integer> iterator = pages.iterator();
            if (iterator.hasNext()) {
                int start = iterator.next();
                int end = start;
                while (iterator.hasNext()) {
                    int next = iterator.next();
                    if (next == end + 1) {
                        end = next;
                    } else {
                        appendRange(sb, start, end);
                        start = next;
                        end = next;
                    }
                }
                appendRange(sb, start, end);
            }
            return sb.toString().trim();
        }

        private void appendRange(StringBuilder sb, int start, int end) {
            if (start == end) {
                sb.append(start);
            } else {
                sb.append(start).append("-").append(end);
            }
            sb.append(", ");
        }
    }

    public void addTerm(String term, int page) {
        for (IndexEntry entry : index) {
            if (entry.term.equals(term)) {
                entry.addPage(page);
                return;
            }
        }
        IndexEntry newEntry = new IndexEntry(term);
        newEntry.addPage(page);
        index.add(newEntry);
    }

    public void removeTerm(String term) {
        index.removeIf(entry -> entry.term.equals(term));
    }

    public void updateTerm(String oldTerm, String newTerm) {
        for (IndexEntry entry : index) {
            if (entry.term.equals(oldTerm)) {
                entry.term = newTerm;
                return;
            }
        }
    }

    public void removePage(int page) {
        for (IndexEntry entry : index) {
            entry.removePage(page);
        }
        index.removeIf(IndexEntry::isEmpty);
    }

    public List<String> searchByPrefix(String prefix) {
        List<String> result = new ArrayList<>();
        for (IndexEntry entry : index) {
            if (entry.term.startsWith(prefix)) {
                result.add(entry.toString());
            }
        }
        return result;
    }

    public void readFromFile(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                String term = parts[0];
                for (int i = 1; i < parts.length; i++) {
                    String[] ranges = parts[i].split(",");
                    for (String range : ranges) {
                        if (range.contains("-")) {
                            String[] interval = range.split("-");
                            int start = Integer.parseInt(interval[0]);
                            int end = Integer.parseInt(interval[1]);
                            for (int page = start; page <= end; page++) {
                                addTerm(term, page);
                            }
                        } else {
                            int page = Integer.parseInt(range);
                            addTerm(term, page);
                        }
                    }
                }
            }
        }
    }

    public void saveToFile(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (IndexEntry entry : index) {
                writer.write(entry.toString());
                writer.newLine();
            }
        }
    }

    public void printIndex() {
        for (IndexEntry entry : index) {
            System.out.println(entry);
        }
    }

    public static void main(String[] args) {
        BookIndex bookIndex = new BookIndex();
        Scanner scanner = new Scanner(System.in);
        String choice;
        boolean running = true;

        do {
            System.out.print("""
                    # --- Menu ---
                    # 1. Add term
                    # 2. Remove term
                    # 3. Update term
                    # 4. Remove page
                    # 5. Search terms
                    # 6. Show index
                    # 7. Save index
                    # 8. Load index
                    # 0. Exit
                    # ->\s""");

            choice = scanner.next();
            scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Insert the term: ");
                    String term = scanner.nextLine();
                    System.out.print("Insert the page: ");
                    int page = scanner.nextInt();
                    bookIndex.addTerm(term, page);
                    System.out.println("Term added to the index successfully.");
                    break;

                case "2":
                    System.out.print("Enter the term to be removed: ");
                    String termRemove = scanner.nextLine();
                    bookIndex.removeTerm(termRemove);
                    System.out.println("Term removed successfully!");
                    break;

                case "3":
                    System.out.print("Enter the old term: ");
                    String oldTerm = scanner.nextLine();
                    System.out.print("Enter the new term: ");
                    String newTerm = scanner.nextLine();
                    bookIndex.updateTerm(oldTerm, newTerm);
                    System.out.println("Term updated successfully!");
                    break;

                case "4":
                    System.out.print("Enter the page number to be removed: ");
                    int pageToRemove = scanner.nextInt();
                    bookIndex.removePage(pageToRemove);
                    System.out.println("Page removed successfully!");
                    break;

                case "5":
                    System.out.print("Enter the prefix to search: ");
                    String prefix = scanner.nextLine();
                    List<String> results = bookIndex.searchByPrefix(prefix);
                    if (results.isEmpty()) {
                        System.out.println("No terms found with the prefix '" + prefix + "'.");
                    } else {
                        System.out.println("Terms found:");
                        for (String res : results) {
                            System.out.println(res);
                        }
                    }
                    break;

                case "6":
                    System.out.println("Current index:");
                    bookIndex.printIndex();
                    break;

                case "7":
                    System.out.print("Enter the filename to save the index: ");
                    String saveFilename = scanner.nextLine();
                    try {
                        bookIndex.saveToFile(saveFilename);
                        System.out.println("Index saved successfully to the file " + saveFilename);
                    } catch (IOException e) {
                        System.out.println("Error saving the index: " + e.getMessage());
                    }
                    break;

                case "8":
                    System.out.print("Enter the filename to load the index: ");
                    String loadFilename = scanner.nextLine();
                    try {
                        bookIndex.readFromFile(loadFilename);
                        System.out.println("Index loaded successfully from the file " + loadFilename);
                    } catch (IOException e) {
                        System.out.println("Error loading the index: " + e.getMessage());
                    }
                    break;

                case "0":
                    System.out.println("Exiting...");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option! Please try again.");
                    break;
            }
        } while (running);

        scanner.close();
    }
}
