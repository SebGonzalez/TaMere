package Client.Util;

import static org.lwjgl.opengl.GL11.*;

import Client.RessourceFactory.*;

import java.awt.Graphics;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class Personnage {
	private static double VITESSE = 2;
	
	private String nom;
	private double x;
	private double y;
	
	private float xVector;
	private float yVector;
	
	private float nbSprite = 0;
	private float position = 0; //0 droite, 1 gauche, 2 haut, 3 bas
	private float cumulDelta = 0;
	private double angle=0;
	
	public Personnage(String nom) {
		this.nom = nom;
		x = 2000;
		y=2000;
	}

	public double getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	//swing
	public void setVecteur(int xSouris, int ySouris, int largeurMap, int hauteurMap, int largeurFenetre, int hauteurFenetre) {
		//System.out.println("SOURIS  "+xSouris + " : " + ySouris);
		System.out.println("DARONNE "+x + " : " + y);
		
		xVector = xSouris - (largeurFenetre/2);
		yVector = ySouris - (hauteurFenetre/2);
		
		double longueur = Math.sqrt(xVector*xVector + yVector*yVector);
		
		xVector/=longueur;
		yVector/=longueur;
		
		System.out.println("VECTOR : "+ xVector +" : " + yVector);
	}
	
	//lwjgl
	public void setVecteur(int xSouris, int ySouris, int largeurMap, int hauteurMap) {
		
		//System.out.println(xSouris + " : " + ySouris);
		
		xVector = xSouris - (Display.getWidth()/2);
		yVector = ySouris - (Display.getHeight()/2);
		angle = Math.toDegrees(Math.atan2(ySouris- (Display.getHeight()/2), xSouris));
		if(xSouris>Display.getWidth()/2) angle=-angle;
		//System.out.println(angle);
		
		Vec2 vector = new Vec2(xVector, yVector);
		
		xVector=vector.x/vector.length();
		yVector=vector.y/vector.length();
		
		if(xSouris > Display.getWidth()/2-50 && xSouris < Display.getWidth()/2+50) {
			if(ySouris < Display.getHeight()/2)
				position = 3;
			else
				position = 2;
			angle = 0;
		}
		else if(xSouris < Display.getWidth()/2)
			position=1;
		else
			position=0;
		
	}
	
	public void move(int largeurMap, int hauteurMap, double delta) {
		
		double deplacementX = x + xVector*delta*VITESSE;
		double deplacementY = y - yVector*delta*VITESSE;
		//System.out.println(deplacementX + ", " + xVector*VITESSE + " : " + deplacementY + ", " + yVector*VITESSE);
		if(deplacementX > 0 && deplacementX < largeurMap)
			x = deplacementX;
		else if(deplacementX < 0)
			x=0;
		else
			x=largeurMap;
		
		if(deplacementY > 0 && deplacementY < hauteurMap)
			y = deplacementY;
		else if(deplacementY < 0)
			y=0;
		else
			y=hauteurMap;
		
	}
	
	public void updatePersonnage(int xSouris, int ySouris, int largeurMap, int hauteurMap, double delta) {
		setVecteur(Mouse.getX(), Mouse.getY(), largeurMap, hauteurMap);
		move(largeurMap, hauteurMap, delta*0.1);
		
		cumulDelta += delta;
		if(cumulDelta > 60) {
			nbSprite++;
			cumulDelta=0;
		}
		if(nbSprite>2)
			nbSprite=0;
		
	}
	
	//swing
	public void drawPersonnage(Graphics g, int largeurFenetre, int hauteurFenetre) {
		g.fillOval(largeurFenetre/2, hauteurFenetre/2, 15, 15);
		g.drawString("Le personnage", largeurFenetre/2-15, hauteurFenetre/2+15 );
	}
	
	//lwjgl
	public void drawPersonnage() {
		glColor3f(1f, 1f, 1f); //reset color
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		//System.out.println("oui");
		RessourcesFactory.getImage(TypeImage.PERSONNAGE).bind();
		
		glPushMatrix();

		glTranslated(Display.getWidth()/2, Display.getHeight()/2, 0.0d);
		
		glRotatef( (float)angle, 0, 0, 1 ); // now rotate
		
		glBegin(GL_QUADS);
		glTexCoord2f(0.20F*nbSprite, 0.20F*position);
		glVertex2i(-25, -25);
		glTexCoord2f(0.20F*(nbSprite+1), 0.20F*position);
		glVertex2i(+100, -25);
		glTexCoord2f(0.20F*(nbSprite+1), 0.20F*(position+1));
		glVertex2i(+100, +100);
		glTexCoord2f(0.20F*nbSprite, 0.20F*(position+1));
		glVertex2i(-25, +100);
		glEnd();
		
		glPopMatrix(); // pop off the rotation and transformation
		glDisable(GL_BLEND);
		
	}
}
