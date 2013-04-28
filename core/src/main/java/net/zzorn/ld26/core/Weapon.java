package net.zzorn.ld26.core;

/**
 *
 */
public class Weapon {

    private float coolDownTime;
    private float coolDown;
    private float shotSize;
    private String textureName;
    private float damage;
    private float velocity;
    private float lifeTime;
    private boolean enemyWeapon;

    public Weapon(float damage, float velocity, float lifeTime, float coolDownTime, float shotSize, String textureName, boolean enemyWeapon) {
        this.damage = damage;
        this.velocity = velocity;
        this.lifeTime = lifeTime;
        this.coolDownTime = coolDownTime;
        this.shotSize = shotSize;
        this.textureName = textureName;
        this.enemyWeapon = enemyWeapon;
    }

    public void update(float deltaTime) {
        if (coolDown > 0) coolDown -= deltaTime;
    }

    public void shoot(Entity host) {
        if (coolDown <= 0) {
            coolDown = coolDownTime;

            // Spawn shot
            final Entity bullet = host.getWorld().addEntity(new Entity(0, 0, 0, shotSize, textureName));
            bullet.getPos().set(host.getPos());
            bullet.getVel().set(host.getDirection()).nor().scl(velocity);
            bullet.getVel().add(host.getVel());
            bullet.setExpiration(lifeTime);
            bullet.setDamage(damage);
            bullet.setHealth(0.1f);
            bullet.setMass(10f);
            bullet.setCollisionGroup(enemyWeapon ? CollisionGroup.ENEMY_BULLETS : CollisionGroup.FRIENDLY_BULLETS);
        }
    }

}
