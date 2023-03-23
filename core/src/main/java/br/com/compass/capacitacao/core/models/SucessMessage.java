package br.com.compass.capacitacao.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class)
public class SucessMessage {

        private String message;

        public SucessMessage(String message) {
            this.message = message;
        }
}
