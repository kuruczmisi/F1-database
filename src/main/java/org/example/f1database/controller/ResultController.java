package org.example.f1database.controller;

import org.example.f1database.entity.Result;
import org.example.f1database.service.ResultService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/results")
public class ResultController {

    private final ResultService resultService;

    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    @GetMapping
    public List<Result> getAllResults() {
        return resultService.getAllResults();
    }

    @GetMapping("/{id}")
    public Result getResultById(@PathVariable Long id) {
        return resultService.getResultById(id);
    }

    @PostMapping
    public Result createResult(@RequestBody Result result) {
        return resultService.createResult(result);
    }

    @PutMapping("/{id}")
    public Result updateResult(@PathVariable Long id,
                               @RequestBody Result result) {
        return resultService.updateResult(id, result);
    }

    @DeleteMapping("/{id}")
    public void deleteResult(@PathVariable Long id) {
        resultService.deleteResult(id);
    }
}