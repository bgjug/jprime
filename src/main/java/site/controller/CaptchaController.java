package site.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/captcha-image")
public class CaptchaController {
	
	/*
	 * Constants
	 */
	public static final String SESSION_PARAM_CAPTCHA_IMAGE = "session_captcha";
	private static final String IMAGE_MIME_TYPE_JPG = "image/jpg";
	private static final String CAPTCHA_FONT = "Arial";
	private static final String REQUEST_PARAM_HEIGHT = "height";
	private static final String REQUEST_PARAM_WIDTH = "width";

	// private static final int MAX_LINES_TO_DRAW = 6;
	// private static final int MIN_LINES_TO_DRAW = 2;
	private static final int MAX_CIRCLES_TO_DRAW = 10;
	private static final int MIN_CIRCLES_TO_DRAW = 6;
	
	private static final Logger logger = LogManager.getLogger(CaptchaController.class);

	@RequestMapping(produces = {
			MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE })
	@ResponseBody
	public byte[] getCaptchaImage(HttpServletRequest request){
		try {

			Color backgroundColor = new Color(240, 244, 247);

			Color textColor = new Color(131, 153, 184);

			Color circleColor = new Color(195, 208, 247);
			Font textFont = new Font(CAPTCHA_FONT, Font.PLAIN, 24);
			int charsToPrint = 5;
			int width = request.getParameter(REQUEST_PARAM_WIDTH) != null
					? Integer.parseInt(request.getParameter(REQUEST_PARAM_WIDTH)) : 150;
			int height = request.getParameter(REQUEST_PARAM_HEIGHT) != null
					? Integer.parseInt(request.getParameter(REQUEST_PARAM_HEIGHT)) : 30;

			float horizMargin = 20.0f;
			float imageQuality = 0.95f; // max is 1.0 (this is for jpeg)
			double rotationRange = 0.7; // this is radians
			BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

			Graphics2D g = (Graphics2D) bufferedImage.getGraphics();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			// Draw an oval
			g.setColor(backgroundColor);
			g.fillRect(0, 0, width, height);

			// lets make some noisy circles
			int circlesToDraw = (int) (Math.random() * (MAX_CIRCLES_TO_DRAW - MIN_CIRCLES_TO_DRAW))
					+ MIN_CIRCLES_TO_DRAW;

			for (int i = 0; i < circlesToDraw; i++) {
				g.setColor((i % 2 == 0) ? (circleColor)
						: (new Color(circleColor.getRed(), 123 + (int) (Math.random() * 122), circleColor.getGreen())));

				int circleWidth = (int) ((Math.random() * width / 2.0) * (Math.random() * 2));
				int circleHeight = (int) ((Math.random() * height / 2.0) * (Math.random() * 2));
				int circleX = (int) (Math.random() * width - circleWidth);
				int circleY = (int) (Math.random() * height - circleHeight);

				g.drawOval(circleX, circleY, circleWidth, circleHeight);
			}

			// // lets make some noisy lines
			// int linesToDraw = (int) (Math.random() * (MAX_LINES_TO_DRAW -
			// MIN_LINES_TO_DRAW)) + MIN_LINES_TO_DRAW;
			// for (int i = 0; i < linesToDraw; i++) {
			// g.setColor((i % 2 == 0) ? (textColor) : (new
			// Color(textColor.getRed(), (int) (Math.random() * 255),
			// textColor.getGreen())));
			//
			// g.drawLine((int) (Math.random() * width), (int) (Math.random() *
			// height),
			// (int) (Math.random() * width), (int) (Math.random() * height));
			// }

			g.setColor(textColor);
			g.setFont(textFont);

			FontMetrics fontMetrics = g.getFontMetrics();
			int maxAdvance = fontMetrics.getMaxAdvance();
			int fontHeight = fontMetrics.getHeight();

			// i removed 1 and l and i because there are confusing to users...
			// Z, z, and N also get confusing when rotated
			// 0, O, and o are also confusing...
			// lowercase G looks a lot like a 9 so i killed it
			// this should ideally be done for every language...
			// i like controlling the characters though because it helps prevent
			// confusion
			String elegibleChars = "123456789";
			char[] chars = elegibleChars.toCharArray();

			float spaceForLetters = -horizMargin * 2 + width;
			float spacePerChar = spaceForLetters / (charsToPrint - 1.0f);

			StringBuffer finalString = new StringBuffer();

			for (int i = 0; i < charsToPrint; i++) {
				double randomValue = Math.random();
				int randomIndex = (int) Math.round(randomValue * (chars.length - 1));

				char characterToShow = chars[randomIndex];
				if (i == 2) {
					characterToShow = '+';
				} else if (i == 4) {
					characterToShow = '=';
				}
				finalString.append(characterToShow);

				// this is a separate canvas used for the character so that
				// we can rotate it independently
				int charWidth = fontMetrics.charWidth(characterToShow);
				int charDim = Math.max(maxAdvance, fontHeight);
				int halfCharDim = (charDim / 2);

				BufferedImage charImage = new BufferedImage(charDim, charDim, BufferedImage.TYPE_INT_ARGB);
				Graphics2D charGraphics = charImage.createGraphics();
				charGraphics.translate(halfCharDim, halfCharDim);
				double angle = (Math.random() - 0.5) * rotationRange;
				if (!(characterToShow == '+' || characterToShow == '=')) {
					charGraphics.transform(AffineTransform.getRotateInstance(angle));
				}
				charGraphics.translate(-halfCharDim, -halfCharDim);
				charGraphics.setColor(textColor);
				charGraphics.setFont(textFont);

				int charX = (int) (0.5 * charDim - 0.5 * charWidth);
				charGraphics.drawString("" + characterToShow, charX,
						((charDim - fontMetrics.getAscent()) / 2 + fontMetrics.getAscent()));

				float x = horizMargin + spacePerChar * (i) - charDim / 2.0f;
				int y = ((height - charDim) / 2);
				// System.out.println("x=" + x + " height=" + height + "
				// charDim=" + charDim + " y=" + y + " advance=" +
				// maxAdvance + " fontHeight=" + fontHeight + " ascent=" +
				// fontMetrics.getAscent());
				g.drawImage(charImage, (int) x, y, charDim, charDim, null, null);

				charGraphics.dispose();
			}

			// Write the image as a jpg
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("JPG");
			if (iter.hasNext()) {
				ImageWriter writer = iter.next();
				ImageWriteParam iwp = writer.getDefaultWriteParam();
				iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
				iwp.setCompressionQuality(imageQuality);
				writer.setOutput(ImageIO.createImageOutputStream(baos));
				IIOImage imageIO = new IIOImage(bufferedImage, null, null);
				writer.write(null, imageIO, iwp);
			} else {
				logger.error("doGet(HttpServletRequest, HttpServletResponse) - no encoder found for jsp.");
				throw new RuntimeException("no encoder found for jsp");
			}
			// ImageIO.write(bufferedImage, "jpg", response.getOutputStream());

			// let's stick the final string in the session
			int firstDigit = Integer.parseInt(finalString.substring(0, 2));
			int secoundDigit = Integer.parseInt(finalString.substring(3, 4));
			int result = firstDigit + secoundDigit;

			logger.debug("Generated captcha code:" + result);

			request.getSession().setAttribute(SESSION_PARAM_CAPTCHA_IMAGE, String.valueOf(result));

			g.dispose();
			baos.flush();
			return baos.toByteArray();
		} catch (IOException ioe) {
			logger.error("doGet(HttpServletRequest, HttpServletResponse)", ioe);
			throw new RuntimeException("Unable to build image", ioe);
		}
	}
	
}
