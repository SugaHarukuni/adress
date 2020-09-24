package com.tuyano.spring;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import java.util.List;




import com.tuyano.spring.repositories.MyDataRepository;

@Controller
public class HelloController {
	
	@Autowired
	MyDataRepository repository;
	/* MyDataRepository�C���X�^���X���t�B�[���h�Ɋ֘A�t���Ă���*/

	@RequestMapping(value="/",method = RequestMethod.GET)
	public ModelAndView index(@ModelAttribute("formModel")MyData mydata,ModelAndView mav) {
		mav.setViewName("index");
		Iterable<MyData> list = repository.findAll();
		mav.addObject("datalist",list);
		return mav;
	}
	/* @ModelAttribute�ŋ�̃G���e�B�e�B�N���X�̃C���X�^���X��p�� */
	
	@RequestMapping(value = "/",method = RequestMethod.POST)
	@Transactional(readOnly=false) /* �f�[�^�x�[�X�̏������ꊇ�ōs���Bread~�͏��������s�Ƃ����Ӗ�*/
	public ModelAndView form(@ModelAttribute("formModel")
							 @Validated MyData mydata,
							 BindingResult result,
							 ModelAndView mav) {
		ModelAndView res = null;
		if(!result.hasErrors()) {
			repository.saveAndFlush(mydata);
			res = new ModelAndView("redirect:/");
		}else {
			mav.setViewName("index");
			Iterable<MyData> list = repository.findAll();
			mav.addObject("msg","���͂ɃG���[������܂�");
			mav.addObject("datalist",list);
			res = mav;
		}
		return res;
	}
	

	
	

	@RequestMapping(value = "/edit/{id}",method = RequestMethod.GET)
	public ModelAndView edit(@ModelAttribute MyData mydata,@PathVariable int id,ModelAndView mav) {
		mav.setViewName("edit");
		mav.addObject("title","�ҏW���");
		Optional<MyData> data = repository.findById((long)id);
		mav.addObject("formModel",data.get());
		return mav;
		
	}
	@RequestMapping(value = "/edit",method = RequestMethod.POST)
	@Transactional(readOnly = false)
	public ModelAndView update(@ModelAttribute MyData mydata) {
		repository.saveAndFlush(mydata);
		return new ModelAndView("redirect:/");
	}
	
	@RequestMapping(value = "/delete/{id}",method = RequestMethod.GET)
	public ModelAndView delete(@PathVariable int id,ModelAndView mav) {
		mav.setViewName("delete");
		mav.addObject("title","�폜���");
		Optional<MyData> data = repository.findById((long)id);
		mav.addObject("formModel",data.get());
		return mav;
		
	}
	@RequestMapping(value = "/delete",method = RequestMethod.POST)
	@Transactional(readOnly = false)
	public ModelAndView remove(@RequestParam long id,ModelAndView mav) {
		repository.deleteById(id);
		return new ModelAndView("redirect:/");
	}
	
	@RequestMapping(value = "/send",method = RequestMethod.POST)
	public ModelAndView send(@RequestParam("value")String value,
							 @RequestParam("type")String type,
							 ModelAndView mav) {
		mav.setViewName("find");
		mav.addObject("title","��������");


		boolean check = isNumber(value);
			if(check && type.equals("id")){
				long id = Integer.parseInt(value);
				Optional<MyData> data = repository.findById((long)id);
				if(!data.isPresent()) {
					boolean hit = true;
					mav.addObject("resultHit",hit);
				}else {
					mav.addObject("formModel",data.get());
				}
			}else if(check==false && type.equals("id")){
				boolean hit = true;
				mav.addObject("resultHit",hit);
			}else if(type.equals("name")) {
				List<MyData> data = repository.findByNameLikeOrderByIdAsc("%"+value+"%");
				mav.addObject("formModel",data);
				if(data.size()==0) {
					boolean hit = true;
					mav.addObject("resultHit",hit);
				}
			}
		return mav;
	}
	
	public boolean isNumber(String num) {
	    try {
	        Integer.parseInt(num);
	        return true;
	        } catch (NumberFormatException e) {
	        return false;
	    }
	}

	

	
}	
	