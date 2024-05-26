package ma.emsi.patientsmvc.web;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import ma.emsi.patientsmvc.entities.Patient;
import ma.emsi.patientsmvc.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import jakarta.validation.constraints.*;

import java.util.List;

@Controller
@AllArgsConstructor
public class patientController {

    private PatientRepository patientRepository;
    @GetMapping(path ="/index")
    public String patients(Model model,
                           @RequestParam(name="page",defaultValue = "0") int page,
                           @RequestParam(name="size",defaultValue = "5") int size,
                           @RequestParam(name="keyword",defaultValue = "") String keyword
    ){
        Page<Patient> pagepatients=patientRepository.findByNomContains(keyword,PageRequest.of(page,size));
        model.addAttribute("listPatients",pagepatients.getContent());
        model.addAttribute("pages",new int [pagepatients.getTotalPages()]);
        model.addAttribute("CurentPage",page);
        model.addAttribute("keyword",keyword);
        return "patients";
    }
    @GetMapping("/delete")
    public String delete(Long id, String keyword, int page){
        patientRepository.deleteById(id);
        return "redirect:/index?page ="+page+"&keyword="+keyword;
    }
    @GetMapping("/")
    public String home(){
        return "redirect:/index";
    }
    @GetMapping("/patients")
    @ResponseBody

    public List<Patient> listPatient(){

        return patientRepository.findAll();
    };
    @GetMapping("/formpatients")
    public  String formPatient(Model model){
        model.addAttribute("patient", new Patient());
        return "formpatients";
    }
    @PostMapping(path = "/save")
    public String save(Model model, @Valid Patient patient, BindingResult bindingResult,
                       @RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "") String keyword){
        if (bindingResult.hasErrors()) return "formpatients";
        patientRepository.save(patient);
        return "redirect:/index?page="+page+"&keyword"+keyword;
    }
    @GetMapping("/editPatients")
    public  String editPatient(Model model, Long id, String keyword,int page){
        Patient patient=patientRepository.findById(id).orElse(null);
        if(patient==null) throw new RuntimeException("Patient introuvable");
        model.addAttribute("patient",patient );
        model.addAttribute("keyword",keyword);
        return "editPatients";
    }
    @GetMapping("/Home")
    public String acceuil() {
        return "Home";
    }

    @GetMapping("/a_propos")
    public String propos() {
        return "a_propos";
    }

    @GetMapping("/contacts")
    public String contacter() {
        return "contacts";
    }
    @GetMapping("/logout")
    public String deconnexion() {
        return "logout";
    }
    @GetMapping("/profile")
    public String consulter() {
        return "profile";
    }



}
