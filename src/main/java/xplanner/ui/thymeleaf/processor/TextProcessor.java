/*-
 * XPlanner is a software to keep track of your working activities
 * Copyright (C) 2021, 2023  Alessandro Iezzi <aiezzi AT alessandroiezzi PERIOD it>
 *
 * XPlanner is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * XPlanner is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with XPlanner.  If not, see <https://www.gnu.org/licenses/>.
 * package xplanner.controller;
 */

package xplanner.ui.thymeleaf.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.*;
import org.thymeleaf.processor.element.*;
import org.thymeleaf.templatemode.TemplateMode;
import org.unbescape.html.HtmlEscape;

import java.util.Map;
import java.util.regex.Pattern;

public class TextProcessor extends AbstractAttributeModelProcessor {
	private static final String ATTR_NAME = "text";
	private static final int PRECEDENCE = 10000;

	private final MessageSource messageSource;

	public TextProcessor(String dialectPrefix, MessageSource messageSource) {
		super(TemplateMode.HTML, /* This processor will apply only to HTML mode */
		      dialectPrefix,     /* Prefix to be applied to name for matching   */
		      null,              /* No tag name: match any tag name             */
		      false,             /* No prefix to be applied to tag name         */
		      ATTR_NAME,         /* Name of the attribute that will be matched  */
		      true,              /* Apply dialect prefix to attribute name      */
		      PRECEDENCE,        /* Precedence (inside dialect's precedence)    */
		      true);             /* Remove the matched attribute afterwards     */

		this.messageSource = messageSource;
	}

	private String processAttribute(ITemplateContext templateContext,
	                                AttributeName attributeName,
	                                String attributeValue,
	                                Map<String, String> attributeMap) {
		if (Pattern.matches("#\\{.*}", attributeValue)) {
			attributeValue = attributeValue.replaceAll("#\\{|}", "");
			attributeValue = messageSource.getMessage(attributeValue, null, "", templateContext.getLocale());

			int index;
			if ((index = attributeValue.indexOf("&")) >= 0 && index < attributeValue.length() - 1) {
				String c = String.valueOf(attributeValue.charAt(index + 1)).toUpperCase();
				attributeMap.put("accesskey", c);
				attributeMap.put("title", "ALT+" + c);
				attributeValue = attributeValue.replaceAll("&", "");
			}
		}

		return attributeValue;
	}

	@Override
	protected void doProcess(ITemplateContext templateContext,
	                         IModel model,
	                         AttributeName attributeName,
	                         String attributeValue,
	                         IElementModelStructureHandler structureHandler) {
		IModelFactory modelFactory = templateContext.getModelFactory();

		boolean openTagProcessed = false;
		boolean closeTagProcessed = false;
		for (int i = 0; i < model.size(); i++) {
			ITemplateEvent templateEvent = model.get(i);

			if (templateEvent instanceof IOpenElementTag && !openTagProcessed) {
				Map<String, String> attributeMap = ((IOpenElementTag) templateEvent).getAttributeMap();
				attributeValue = processAttribute(templateContext, attributeName, attributeValue, attributeMap);

				IOpenElementTag openEle = modelFactory
						.createOpenElementTag(((IOpenElementTag) templateEvent).getElementCompleteName(),
						                      attributeMap,
						                      AttributeValueQuotes.DOUBLE,
						                      false);
				model.replace(i, openEle);

				openTagProcessed = true;
			}
		}

		if (model.get(model.size() - 1) instanceof ICloseElementTag) {
			model.insert(model.size() - 1, modelFactory.createText(attributeValue));
		}
	}

	//	@Override
//	protected void doProcess(ITemplateContext templateContext,
//	                         IProcessableElementTag processableElementTag,
//	                         AttributeName attributeName,
//	                         String attributeValue,
//	                         IElementTagStructureHandler structureHandler) {
//		if (Pattern.matches("#\\{.*}", attributeValue)) {
//			attributeValue = attributeValue.replaceAll("#\\{|}", "");
//			attributeValue = messageSource.getMessage(attributeValue, null, "", templateContext.getLocale());
//
//			int index;
//			if ((index = attributeValue.indexOf("&")) >= 0 && index < attributeValue.length() - 1) {
//				String c = String.valueOf(attributeValue.charAt(index + 1)).toUpperCase();
//				structureHandler.setAttribute("accesskey", c);
//				structureHandler.setAttribute("title", "ALT+" + c);
//				attributeValue = attributeValue.replaceAll("&", "");
//			}
//		}

//		structureHandler.setBody(HtmlEscape.escapeHtml5(attributeValue), false);
//	}
}
