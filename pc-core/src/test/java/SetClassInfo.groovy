import org.junit.Test

class SetClassInfo {
    Set<String> set = new HashSet<>()

    @Test
    public void rr() {
        SetClassInfo sci = new SetClassInfo();
        sci.read(new File("C:\\Users\\wangl\\IdeaProjects\\process-control\\src\\test\\resources\\classload.log"))
        sci.print("java", "ddbm")

    }

    public void read(File file) {
        FileReader reader = new FileReader(file);
        String line = null;
        while ((line = reader.readLine()) != null) {
            try {
                if (line.trim().equals("")) {
                    continue
                }
                String[] splits = line.split("\\s|:")
                if (!splits[1].contains("\$")) {
                    set.add(splits[1])
                } else {
                    String clz = splits[1];
                    clz = clz.substring(0, clz.indexOf('$'))
                    set.add(clz)
                }

            } catch (Throwable e) {
                e.printStackTrace()
                set.add(line)
            }
        }
    }

    public void print(String... filters) {
        set.each { l ->
            if (filters == null) {
                println(l)
            } else {
                for (String f : filters) {
                    if (l.contains(f)) {
                        println(l)
                    }
                }
            }
        }
    }

}
