package tsvetkov.daniil.search.util;

import org.springframework.data.elasticsearch.core.suggest.Completion;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class EnhancedCompletion extends Completion {

    public EnhancedCompletion() {
        super();
    }

    public EnhancedCompletion(String[] input) {
        super(input);
    }

    public EnhancedCompletion(List<String> input) {
        super(input);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Completion that)) return false;
        return Arrays.equals(getInput(), that.getInput()) &&
                Objects.equals(getContexts(), that.getContexts()) &&
                Objects.equals(getWeight(), that.getWeight());
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(this.getInput());
        result = 31 * result + Objects.hashCode(this.getContexts());
        result = 31 * result + Objects.hashCode(this.getWeight());
        return result;
    }

    @Override
    public String toString() {
        return "Completion{" +
                "input=" + Arrays.toString(this.getInput()) +
                ", contexts=" + this.getContexts() +
                ", weight=" + this.getWeight() +
                '}';
    }
}
