package net.mindengine.galen.specs.reader.page;

import net.mindengine.galen.specs.page.ObjectSpecs;
import net.mindengine.galen.specs.page.PageSection;
import net.mindengine.galen.specs.reader.IncorrectSpecException;
import net.mindengine.galen.specs.reader.SpecReader;

public class StateDoingSection extends State {

    private PageSection section;
    private ObjectSpecs currentObjectSpecs;
    
    private SpecReader specReader = new SpecReader();
    public StateDoingSection(PageSection section) {
        this.section = section;
    }

    @Override
    public void process(String line) {
        if (startsWithIndentation(line)) {
            if (currentObjectSpecs == null) {
                throw new IncorrectSpecException("There is no object defined in section");
            }
            else {
                try {
                    currentObjectSpecs.getSpecs().add(specReader.read(line.trim()));
                }
                catch (IncorrectSpecException exception) {
                    throw new IncorrectSpecException("Incorrect spec for object \"" + currentObjectSpecs.getObjectName() + "\"", exception);
                }
            }
        }
        else {
            beginNewObject(line);
        }
    }

    private void beginNewObject(String line) {
        String name = line.trim().replace(":", "");
        currentObjectSpecs = new ObjectSpecs(name);
        section.getObjects().add(currentObjectSpecs);
    }

    private boolean startsWithIndentation(String line) {
        return line.startsWith("\t") || line.startsWith("  ");
    }

}