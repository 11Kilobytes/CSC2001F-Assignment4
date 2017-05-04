# Location of sources and classes
SOURCE_DIR  := src
TEST_DIR    := test
OUTPUT_DIR  := bin
REPORT_DIR  := coveragereport
DOC_DIR     := doc
CLEAN_DIRS  := $(REPORT_DIR) $(DOC_DIR) $(OUTPUT_DIR)
CLEAN_FILES := jacoco.exec

# Unix tools
GREP     := grep
MKDIR    := mkdir -p
RM       := rm -rf
SHELL    := bash
JAVAC    := javac
FIND     := find
AWK      := awk
JAVA     := java
JAVADOC  := javadoc
SED      := sed

# Java Settings
JFLAGS  := -sourcepath $(SOURCE_DIR) -d $(OUTPUT_DIR)
JDFLAGS := -sourcepath $(SOURCE_DIR) -d $(DOC_DIR) -link https://https://docs.oracle.com/javase/8/docs/api

# Dependencies and Classpath
HAMCREST_JAR := lib/junit/hamcrest-core-1.3.jar
JUNIT_JAR    := lib/junit/junit-4.12.jar
ASM          := lib/asm/asm-5.0.4.jar
ASM_COMMONS  := lib/asm/asm-commons-5.0.4.jar
ASM_TREE     := lib/asm/asm-tree-5.0.4.jar

JACOCO_CORE   := lib/jacoco/org.jacoco.core-0.7.5.201505241946.jar
JACOCO_REPORT := lib/jacoco/org.jacoco.report-0.7.5.201505241946.jar
JACOCO_AGENT  := lib/jacoco/jacocoagent.jar
UCT_REPORT    := lib/tools

COMMONS_CLI := lib/cli/commons-cli-1.3.1.jar

class_path := \
 OUTPUT_DIR \
 HAMCREST_JAR JUNIT_JAR \
 ASM ASM_COMMONS ASM_TREE \
 COMMONS_CLI JACOCO_CORE JACOCO_REPORT \
 UCT_REPORT

# $(call build-classpath, variable-list)
# Takes a list of variable names variable-list and returns
# a bash/zsh list that can be assigned to $CLASSPATH
# Each variable in variable-list must contain a *single* jar file path.
define build-classpath
$(strip                                                \
	$(patsubst :%,%,                                     \
		$(subst : ,:,																		   \
			$(strip																					 \
				$(foreach j, $1, $(call get-file,$j):)))))
endef

# $(call getfile, variable-name) tries to find the file
# stored in the variable with the given name, and returns it if
# it is found or otherwise prints a warning.
define get-file
	$(strip                                             \
		$($1)                                             \
		$(if $(call file-exists-eval,$1),, 							  \
			$(warning The file referenced by the variable   \
								'$1', ($($1)) cannot be found)))
endef

# $(call file-exists-eval, variable-name)
# Returns the value of the variable with the given name (variable-name)
# and prints a warning if the variable is empty
define file-exists-eval
	$(strip                                          \
		$(if $($1),, $(warning '$1' has no value))     \
		$(wildcard $($1)))
endef

# Default target
all: compile_sources compile_tests

# Set the Java CLASSPATH
export CLASSPATH := $(call build-classpath,$(class_path))

# Java Sources
all_java_sources := $(OUTPUT_DIR)/all.javas

.INTERMEDIATE: $(all_java_sources)
$(all_java_sources):
	($(FIND) $(SOURCE_DIR) -type f -name '*.java' | $(GREP) -v ".*Test.java") > $@

.PHONY: compile_sources
compile_sources: $(all_java_sources)
	$(JAVAC) $(JFLAGS) @$<

# Java Tests
all_java_tests := $(OUTPUT_DIR)/tests.javas

.INTERMEDIATE: $(all_java_tests)
$(all_java_tests):
	$(FIND) $(TEST_DIR) -type f -name '*Test.java' > $@

all_test_classes := $(OUTPUT_DIR)/test.classes

.INTERMEDIATE: $(all_test_classes)
$(all_test_classes):
	$(FIND) $(OUTPUT_DIR) -type f -name '*Test.class' \
		| $(SED) 's/.class/ /' \
		| $(AWK) -F/ '{printf $$NF}' \
    > $@

## Compile JUnit test classes
.PHONY: compile_tests
compile_tests: $(all_java_tests)
	$(JAVAC) $(JFLAGS) @$<

## Run JUnit test classes
.PHONY: test
test: compile_tests $(all_test_classes)
	$(JAVA) -javaagent:$(JACOCO_AGENT) \
		org.junit.runner.JUnitCore       \
		$$(cat $(all_test_classes))

# Print out the auto-generated classpath
.PHONY: classpath
classpath:
	@echo CLASSPATH='$(CLASSPATH)'

# Generate Javadoc for all sources
.PHONY: doc
doc: $(all_java_sources)
	$(JAVADOC) $(JDFLAGS) @$<

.PHONY: clean
clean:
	$(RM) $(CLEAN_FILES)
	$(foreach dir, $(CLEAN_DIRS),$(RM) $(dir)/*)

# Jacoco
.PHONY: report
report: test
	$(JAVA) Report .
