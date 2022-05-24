package org.lviv.crime.controller.index;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.result.view.Rendering;

/**
 * Redirects to the Swagger UI page.
 */
@Controller
public class IndexController {

    @GetMapping(value = {"", "index.html"})
    public Rendering redirectToApiDocumentation() {
        return Rendering.redirectTo("swagger-ui/").build();
    }
}
