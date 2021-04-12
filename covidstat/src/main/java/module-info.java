module covidstat {
    requires javafx.controls;
    requires com.fasterxml.jackson.databind;
    requires java.net.http;
    exports com.kamilachyla.model to com.fasterxml.jackson.databind;
    opens com.kamilachyla.model to com.fasterxml.jackson.databind;

    exports com.kamilachyla;
}